package com.example.semproject.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semproject.model.ProductModel
import com.example.semproject.repository.ProductRepository

class ProductViewModel(
    private val repo: ProductRepository) : ViewModel() {
    fun addTask(taskModel: ProductModel, callback:(Boolean, String)->Unit){
        repo.addProduct(taskModel,callback)
    }
    fun updateTask(taskId:String,data:MutableMap<String,Any>,callback:(Boolean,String)->Unit){
        repo.updateProduct(taskId,data){ success, message ->
            if (success) {
                getAllTasks() // Refresh tasks after update
            }
            callback(success, message)
        }
    }
    fun deleteTask(taskId:String,callback:(Boolean,String)->Unit){
        repo.deleteProduct(taskId,callback)
    }





    var _tasks= MutableLiveData<ProductModel?>()
    var tasks= MutableLiveData<ProductModel?>()
        get()=_tasks

    var _allTasks= MutableLiveData<List<ProductModel>>()
    var allTasks= MutableLiveData<List<ProductModel>>()
        get()=_allTasks

    fun getTaskById(taskId:String){
        repo.getProductById(taskId){
                model,success,message->
            if (success){
                _tasks.value=model
            }
        }
    }


    var _loading= MutableLiveData<Boolean>()
    var loading= MutableLiveData<Boolean>()
        get()=_loading

    fun getAllTasks(){
        repo.getAllProducts() {product,success,message->
            if (success){
                _allTasks.value=product
                _loading.value = false
            }
        }
    }


    fun moveTask(taskId: String, callback: (Boolean, String) -> Unit) {
        loading.value = true
        repo.moveTask(taskId) { success, message ->
            loading.value = false
            if (success) {
                getAllTasks() // Refresh tasks
                getCompletedTasks() // Refresh completed tasks
            }
            callback(success, message)
        }
    }


    fun moveCompTask(taskId: String, callback: (Boolean, String) -> Unit) {
        loading.value = true
        repo.moveCompTask(taskId) { success, message ->
            loading.value = false
            if (success) {
                getAllTasks() // Refresh tasks
                getCompletedTasks() // Refresh completed tasks
            }
            callback(success, message)
        }
    }




    private val _completedTasks = MutableLiveData<List<ProductModel>>()
    val completedTasks: LiveData<List<ProductModel>> get() = _completedTasks

    fun deleteCompTask(taskId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(taskId, callback) // Ensure repo calls delete from "completedTasks"
    }

    fun getCompletedTasks() {
        repo.getCompletedTasks { tasks, success, _ ->
            if (success) {
                _completedTasks.value = tasks
            }
        }
    }

    fun deleteAllTasks(callback: (Boolean, String) -> Unit) {
        repo.deleteAllTask(callback)
    }
    }
