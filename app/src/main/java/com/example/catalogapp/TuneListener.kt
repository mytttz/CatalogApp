package com.example.catalogapp

import com.example.catalogapp.dataclasses.Tune

interface TuneListener {
    fun onTuneCreated(tune: Tune)
}