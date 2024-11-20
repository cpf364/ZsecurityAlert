plugins {
    id("com.android.application")
}

android {
    namespace = "com.zebra.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zebra.demo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures{
        aidl = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    gradle.projectsEvaluated {
        tasks.withType<JavaCompile> {
            val f: Set<File>? = options.bootstrapClasspath?.files
            val l = mutableListOf<File>()

            println(System.getProperty("user.dir"))
            l.add(File(System.getProperty("user.dir") + "/app/libs/a14_framwork.jar"))

            if (f != null) {
                l.addAll(f)
            }

            options.bootstrapClasspath = files(*l.toTypedArray())
        }
    }


}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation( "com.google.code.gson:gson:2.11.0")
    // Material Design Components
    compileOnly(files("libs/a14_framwork.jar"))

    implementation( "com.google.android.material:material:1.9.0")

    implementation( "androidx.recyclerview:recyclerview:1.2.1")

    implementation( "androidx.cardview:cardview:1.0.0")

    // ConstraintLayout for flexible layouts
    implementation( "androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.dagger:dagger:2.28.3")
    annotationProcessor ("com.google.dagger:dagger-compiler:2.28.3")
}