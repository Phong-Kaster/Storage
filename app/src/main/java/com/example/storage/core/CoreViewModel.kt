package com.example.storage.core

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class CoreViewModel
@Inject
constructor() : ViewModel() {
     val TAG: String = this.javaClass.simpleName
}