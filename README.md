# Android Ui Tester

Thanks to [Kata Jona](https://github.com/katajona) for the collaboration

This Project is an example UI test setup for Android. It can be configured to use Robolectric and Espresso at the same time. We make it easer to write UI tests with Android's new library: Activity Scenario and Fragment Scenario. The example Project is implemented with Koin, but could be replaced with other dependency injection libraries. We use the robot pattern to write easier test by having a class which contains every action which a screen could have.

The library is available in jitpack.

## Jitpack

Add jitpack to your gradle file to be able to use the library:
````
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
````

## Usage:

You can then add the library as a gradle dependency.
````
androidTestImplementation 'com.github.Jose-Luis-Nunez:Android-Ui-Tester:1.0.1'
````

If you don't want to add all the necessary dependencies you can copy the already prepared ````testing_dependencies.gradle```` gradle file and apply it:
````
apply from: '../testing_dependencies.gradle'
````

Or you can import it to your Application
````
dependencies {
    //  This is required to fix databing error with Android tests
    kaptAndroidTest "androidx.databinding:databinding-compiler:$android_gradle_plugin"
    kaptTest "androidx.databinding:databinding-compiler:$android_gradle_plugin"

    androidTestImplementation "org.mockito:mockito-android:$mockito_version"
    androidTestUtil "androidx.test:orchestrator:$orchestrator_version"
    androidTestImplementation "org.robolectric:annotations:$robolectric_version"
    testImplementation "org.robolectric:robolectric:$robolectric_version"

    // Once https://issuetracker.google.com/127986458 is fixed this can be testImplementation and put inside our library
    debugImplementation "androidx.fragment:fragment-testing:$fragment_version"
}
````

For the Activity/Fragment that we want to test we need to create a Robot that implements `BaseRobot()`. Inside the robot we need to set up the Activity/Fragment that we want to test and need to set up the dependency injection tool that we are using to mock the necessary files.

````
class MainRobot : BaseRobot() {

    private val viewModel: MainViewModel = mainViewModelMock()
    private val textUtil = textUtilMock()

    private lateinit var scenario: ActivityScenario<MainActivity>

    private val textTitle = MutableLiveData<String>()
    private val textSubTitle = MutableLiveData<String>()

    override fun setupInjections() {
        startKoin {
          androidContext(ApplicationProvider.getApplicationContext())
          modules(module {
            viewModel { viewModel }
            single { textUtil }
            })
        }
    }

    override fun setupScenario() {
        scenario = setupActivityScenario()
    }

    override fun stopDependencyInjection() {
        stopKoin()
    }

    private fun mainViewModelMock(): MainViewModel {
        val viewModel = mock(MainViewModel::class.java)
        given(viewModel.textTitle).will { textTitle }
        given(viewModel.textSubTitle).will { textSubTitle }
        return viewModel
    }

    private fun textUtilMock(): TextUtil {
        val textUtil = mock(TextUtil::class.java)
        given(textUtil.welcomeText()).will { "Mock" }
        return textUtil
    }
}
````

### setupKoinModule

To setup the koin you can create a helper method in your project. Or you can replace this with any other dependency injection tool.
````
/**
 * Setting up several passed mocked view models to be used by the fragment or activity
 * The module declaration allows to use the dsl from view model like -> viewModel { mockedViewmodel }
 */
fun setupKoinModule(moduleDeclaration: ModuleDeclaration) {
    startKoin {
        androidContext(ApplicationProvider.getApplicationContext())
        loadKoinModules(module(moduleDeclaration = moduleDeclaration))
    }
}
````

### Test cases

After setting up the Robot we can start with writing our test cases inside the robot. Our test could be as simple like this:

````
fun postTitleText(text: String?) {
    textTitle.postValue(text)
}

fun verifyTitleIsDisplayed(message: String) {
    isDisplayedWithText(R.id.title, message)
}

fun click() {
    clickOnView(R.id.button)
}
````

As you can see most of the functions are predefined in the `BaseRobot` and you can simply write `isDisplayedWithText(R.id.title, message)` instead of writing `getView(R.id.title).check(matches(isDisplayed())).check(matches(withText(message)))`
This class can be extended any time to your needs, to reduce redundant code while testing.

After we finished our Robot we can write the UI test. In the UI test we just need to specify which robot we want to use.
Here is an example how to do this:

````
class MainUiTest {

    @Test
    fun testTitle() {
        with<MainRobot> {
            val title = "title"
            postTitleText(title)
            verifyTitleIsDisplayed(title)
        }
    }
}
````
For more example you can look into the app.

## Extra setup tips

#### Setting up mockito
For mockito mock maker use the setup in you gradle:
````
android {
     packagingOptions {
           pickFirst 'mockito-extensions/org.mockito.plugins.MockMaker'
     }
}
````

#### DataBindig
If you are using DataBinding you need to set it up also in gradle.
````
android {
    dataBinding {
        enabled = true
        enabledForTests = true
    }
}
````
If you are using DataBinding and Robolectric it can sometimes get stuck so I would recommend using `@LooperMode(LooperMode.Mode.PAUSED)` annotation for your UI test:
````
@LooperMode(LooperMode.Mode.PAUSED)
class MainUiTest {}
````

#### Replacing Application setup, clear state
You need to replace the Application setup so that dependency injection is not initialized so you can use your mocked classes. You can do this by setting `AppAndroidJUnitRunner` from the testing folder to be your `testInstrumentationRunnerArguments` . Also here you can set to clear the states between test.
````
defaultConfig {
    testInstrumentationRunner "com.example.testingapp.testing.ui.AppAndroidJUnitRunner"
    // The following argument makes the Android Test Orchestrator run its
    // "pm clear" command after each test invocation. This command ensures
    // that the app's state is completely cleared between tests.
      testInstrumentationRunnerArguments clearPackageData: 'true'
}
````
#### Executing with AndroidX test Orchestrator
````
testOptions {
      animationsDisabled true
      execution 'ANDROIDX_TEST_ORCHESTRATOR'
      unitTests {
          returnDefaultValues = true      
          includeAndroidResources = true
      }
}
````

#### Running tests with Robolectric and Espresso
To run your tests with Robolectric and Espresso you can create a a folder `sharedTest` and put all of your test here. You also need to set up this configuration in gradle.
````
sourceSets {
    String sharedTestDir = "${projectDir}/src/sharedTest/kotlin"
    main.java.srcDirs += "${projectDir}/src/main/kotlin"
    androidTest {
        java.srcDirs += "${projectDir}/src/androidTest/kotlin"
        java.srcDirs += sharedTestDir
    }
}
````

#### Making your ViewModel open
For testing you need to make your viewModel open for testing you could do it with `@OpenForTest` annotation.
For this you need to include this in to your gradle file:
````
classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlin_version")

allOpen {
    annotation("com.example.testingapp.OpenForTest")
}
````
