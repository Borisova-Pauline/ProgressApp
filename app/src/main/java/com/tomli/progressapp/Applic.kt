package com.tomli.progressapp

import android.app.Application
import com.tomli.progressapp.databases.ProgressDB

class Applic: Application() {
    val database by lazy{ ProgressDB.createDB(this) }
}