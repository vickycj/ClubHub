package com.vicky.apps.datapoints.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vicky.apps.datapoints.common.SchedulerProvider
import com.vicky.apps.datapoints.data.remote.Repository
import com.vicky.apps.datapoints.ui.model.CompanyDetails
import com.vicky.apps.datapoints.ui.model.ResponseData
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy


class MainViewModel(private val repository: Repository,
                    private val schedulerProvider: SchedulerProvider
):ViewModel() {




    private val response: MutableLiveData<Boolean> = MutableLiveData()

    fun getSubscription():MutableLiveData<Boolean> = response

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var responseData:List<ResponseData>

    private var companyDetails:MutableList<CompanyDetails> = ArrayList()

    private var ascendingVal:Boolean = false

    fun setCompositeData(compositeDisposable: CompositeDisposable) {
        this.compositeDisposable = compositeDisposable
    }



    fun getDataFromRemote() {

        compositeDisposable.add(generateApiCall().subscribeBy ( onSuccess = {
            this.responseData = it
            Log.d("responseData",responseData.size.toString())
            updateTheValuesToUI()
        }, onError = {
            Log.d("valuessss",it.message)
        } ))


    }

    fun setCompanyDetails(companyDetails: MutableList<CompanyDetails>){
        this.companyDetails = companyDetails
    }


    fun getCompanyDetails():List<CompanyDetails>{
        return companyDetails
    }

    private fun updateTheValuesToUI() {
        responseData.forEach {
            companyDetails.add(CompanyDetails(it._id, it.logo,it.company))
        }
        response.postValue(true)
    }


    fun generateApiCall():Single<List<ResponseData>>{
        return repository.getDataFromApi()
            .compose(schedulerProvider.getSchedulersForSingle())
    }

    fun sortCompanyData(){
         if(!ascendingVal){
            companyDetails = ArrayList(companyDetails).sortedBy {
                it.name
            }.toMutableList()
           ascendingVal= true
        }else{
            companyDetails = ArrayList(companyDetails).sortedByDescending {
                it.name
            }.toMutableList()
             ascendingVal=  false
        }
    }

     fun filterCompany(text:String?): List<CompanyDetails>{
         if(text == null) return companyDetails
         var textVal = text.toUpperCase()
        return ArrayList(companyDetails).filter {
            it.name.startsWith(textVal)
        }.toMutableList()
    }

    fun findCSingleCompanyData(id:String):ResponseData?{
        return responseData.find {
            it._id == id
        }
    }





}