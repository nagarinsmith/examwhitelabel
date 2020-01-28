object Dependencies {

    val androidDependencies: Iterable<String> = listOf(
        "androidx.core:core-ktx:${Versions.ktx}",
        "com.google.android.material:material:${Versions.material}",
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}",
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}",
        "org.koin:koin-android-viewmodel:${Versions.koin}",
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}",
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}",
        "com.squareup.retrofit2:retrofit:${Versions.retrofit}",
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit}",
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}",
        "androidx.room:room-runtime:${Versions.room}",
        "androidx.room:room-ktx:${Versions.room}",
        "com.tinder.scarlet:scarlet:${Versions.scarlet}",
        "com.tinder.scarlet:websocket-okhttp:${Versions.scarlet}",
        "com.tinder.scarlet:message-adapter-gson:${Versions.scarlet}",
        "com.tinder.scarlet:stream-adapter-coroutines:${Versions.scarlet}"
    )

    val androidKapt: Iterable<String> = listOf(
        "androidx.room:room-compiler:${Versions.room}"
    )
}
