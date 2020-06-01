cmake_minimum_required(VERSION 3.4.1)

file(GLOB_RECURSE GIF_DIRECTORIES ${CMAKE_SOURCE_DIR}/giflib/*.*)

add_library(
        gifplayer
        SHARED
        PthreadSleep.cpp
        SyncTime.cpp
        GifPlayer.cpp
        GifJni.cpp
        ${GIF_DIRECTORIES})


target_link_libraries(
        gifplayer
        jnigraphics
        log
        android)