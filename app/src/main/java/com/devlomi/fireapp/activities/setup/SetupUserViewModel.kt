package com.devlomi.fireapp.activities.setup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.devlomi.fireapp.R
import com.devlomi.fireapp.activities.main.messaging.ChatActivity
import com.devlomi.fireapp.exceptions.NoDefaultImageException
import com.devlomi.fireapp.extensions.getFileRx
import com.devlomi.fireapp.extensions.observeSingleValueEvent
import com.devlomi.fireapp.extensions.updateChildrenRx
import com.devlomi.fireapp.model.realms.CurrentUserInfo
import com.devlomi.fireapp.utils.*
import com.devlomi.fireapp.utils.network.BroadcastManager
import com.devlomi.fireapp.utils.network.FireManager
import com.devlomi.fireapp.utils.network.GroupManager
import com.google.firebase.storage.FirebaseStorage
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.HashMap

class SetupUserViewModel : ViewModel() {
    private var currentUserPhotoUrl = ""
    private var currentUserPhotoThumb = ""
    private val disposables = CompositeDisposable()
    private val fireManager = FireManager()
    private val groupManager = GroupManager()
    private val broadcastManager = BroadcastManager()
    private val _completeSetupLiveData = MutableLiveData<Pair<Boolean, Throwable?>>()
    private var fetchUserImageDisposable: Disposable? = null



    val completeSetupLiveData: LiveData<Pair<Boolean, Throwable?>>
        get() = _completeSetupLiveData

    private val _loadUserImage = MutableLiveData<String>()
    val loadUserImageLiveData: LiveData<String>
        get() = _loadUserImage

    fun fetchUserImage() {
        val disposable = getUserImage().subscribe {
            val photoUrl = it.first
            _loadUserImage.value = photoUrl
        }

        fetchUserImageDisposable = disposable
        disposables.add(disposable)
    }

    private fun getUserInfoHashmap(userName: String, thumbImg: String, photoUrl: String?, filePath: String? = null): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        map["photo"] = photoUrl!!
        map["name"] = userName
        map["phone"] = FireManager.phoneNumber
        val defaultStatus = String.format(MyApp.context().getString(R.string.default_status), MyApp.context().getString(R.string.app_name))
        map["status"] = defaultStatus
        val appVersion = AppVerUtil.getAppVersion(MyApp.context())
        if (appVersion != "")
            map["ver"] = appVersion

        //create thumbImg and original image and compress them if the user chosen a new photo
        if (filePath != null) {
            val circleBitmap = BitmapUtils.getCircleBitmap(BitmapUtils.convertFileImageToBitmap(filePath))
            val thumbImg = BitmapUtils.decodeImageAsPng(circleBitmap)
            map["thumbImg"] = thumbImg
        } else {
            map["thumbImg"] = thumbImg
        }


        return map
    }

    //save user info locally
    private fun saveUserInfo(photoFile: String, thumbImg: String?, userName: String) {
        SharedPreferencesManager.saveMyPhoto(photoFile)
        if (thumbImg.isNullOrEmpty()) {
            val circleBitmap = BitmapUtils.getCircleBitmap(BitmapUtils.convertFileImageToBitmap(photoFile))
            val thumbImg = BitmapUtils.decodeImageAsPng(circleBitmap)
            SharedPreferencesManager.saveMyThumbImg(thumbImg)
        } else {
            SharedPreferencesManager.saveMyThumbImg(thumbImg)
        }

        SharedPreferencesManager.saveMyUsername(userName)
        SharedPreferencesManager.savePhoneNumber(FireManager.phoneNumber)
        val defaultStatus = String.format(MyApp.context().getString(R.string.default_status), MyApp.context().getString(R.string.app_name))
        SharedPreferencesManager.saveMyStatus(defaultStatus)
        SharedPreferencesManager.setAppVersionSaved(true)
        saveCountryCode()


    }


    //save country code to shared preferences (see ContactUtils class for more info)
    private fun saveCountryCode() {
        val phoneUtil = PhoneNumberUtil.createInstance(MyApp.context())
        val numberProto: Phonenumber.PhoneNumber
        try {
            //get the countryName code Like "+1 or +44 etc.." from the user number
            //so if the user number is like +1 444-444-44 we will save only "+1"
            numberProto = phoneUtil.parse(FireManager.phoneNumber, "")
            val countryCode = phoneUtil.getRegionCodeForNumber(numberProto)
            SharedPreferencesManager.saveCountryCode(countryCode)
        } catch (e: NumberParseException) {
            e.printStackTrace()
        }

    }


    private fun hitApi(sponserId: String, contxt: Any) {

        val mobile = FireManager.phoneNumber.replace("+91","");
        /*       if (editText.getText().toString().equals("") || mobile.equals("")) {
            return;
        }*/
        try {
            val requestQueue = Volley.newRequestQueue(contxt as Context?)
            val URL = "http://sh003.hostgator.tempwebhost.net/~grocelmj/pos/api/registerBPC"
            val jsonBody = JSONObject()
            jsonBody.put("sponser_id", sponserId)
            jsonBody.put("mobile", mobile)
            val requestBody = jsonBody.toString()

            val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                    Response.Listener { response ->
                        //                        Toast.makeText(Login_screen.this,"String Response : "+ response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            Log.d("JSON", response.toString())
                            // loading.dismiss();
                            /*  String Error = response.getString("httpStatus");
                                if (Error.equals("")||Error.equals(null)){

                                }else if(Error.equals("OK")){
                                    JSONObject body = response.getJSONObject("body");

                                }else {

                                }*/

                            if (response.getString("status").equals("ok", ignoreCase = true)) {

                               // showSnackbarMsg("Welcome!");

                            } else {
                               // showSnackbarMsg(response.getString("message"));
                                /* InterfaceManager.shared().a.startMainActivity(ProfileActivity.this, extras);
                                    finish();*/
                                VolleyLog.d("Error", "Error: " + response.getString("message"))
                            }


                        } catch (e: JSONException) {
                            e.printStackTrace()
                            // loading.dismiss();
                        }

                        //                        resultTextView.setText("String Response : "+ response.toString());
                    }, Response.ErrorListener { error ->
                // loading.dismiss();
                VolleyLog.d("Error", "Error: " + error.message)
                //Toast.makeText(Login_screen.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            })

            requestQueue.add(jsonObjectRequest)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
    fun completeSetup(imagePath: String?, userName: String,sponserId:String,contxt:Any) {
        //upload this image

        val observable = if (imagePath != null) {
            completeSetupWithPickedImage(imagePath, userName)
        } else {
            if (currentUserPhotoUrl != "") {
                //download this image locally
                completeSetupWithRemotePhotoExists(userName)
            } else {
                completeSetupWithNoRemotePhoto(userName)
            }
        }

        observable.subscribe({}, { throwable ->
            _completeSetupLiveData.value = Pair(false, throwable)
        }, {
            //onComplete
            RealmHelper.getInstance().saveObjectToRealm(CurrentUserInfo(FireManager.uid, FireManager.phoneNumber))
            SharedPreferencesManager.setUserInfoSaved(true)


           disposables.add(groupManager.createPublicChatGroup(ChatActivity.PUBLIC_GROUP_ID).subscribe({}) { throwable: Throwable? -> });

            if(!sponserId.equals("")){
                hitApi(sponserId,contxt);
            }

            _completeSetupLiveData.value = Pair(true, null)
        }).addTo(disposables)
    }

    private fun completeSetupWithNoRemotePhoto(userName: String): Observable<Any> {
        //cancel old process if exists to start a new one
        fetchUserImageDisposable?.dispose()


        val fetchGroups = groupManager.fetchUserGroups()
        val fetchBroadcasts = broadcastManager.fetchBroadcasts(FireManager.uid)


        //if the old photo not exists on server(this is the first time)
        //download the 'defaultUserProfilePhoto'
        return getDefaultUserProfilePhoto().map { triple ->

            val localPhotoUrl = triple.first
            val photoUrl = triple.second
            val thumb = triple.third


            val number = FireManager.phoneNumber


            saveUserInfo(localPhotoUrl, thumb, userName)


            return@map getUserInfoHashmap(userName, thumb, photoUrl, localPhotoUrl)
        }.flatMap { userInfoMap ->

            val setUserInfo = FireConstants.usersRef.child(FireManager.uid).updateChildrenRx(userInfoMap).toObservable<Any>()


            return@flatMap Observable.merge(fetchGroups, fetchBroadcasts, setUserInfo)
        }
    }

    private fun completeSetupWithRemotePhotoExists(userName: String): Observable<Any> {
        return fireManager.downloadCurrentUserPhoto(currentUserPhotoUrl).toObservable().map { localPhotoPath ->


            saveUserInfo(localPhotoPath, currentUserPhotoThumb, userName)

            return@map localPhotoPath
        }.flatMap { localPhotoPath ->
            val fetchGroups = groupManager.fetchUserGroups()
            val fetchBroadcasts = broadcastManager.fetchBroadcasts(FireManager.uid)

            val userDict = getUserInfoHashmap(userName, currentUserPhotoThumb, currentUserPhotoUrl, localPhotoPath)

            //set user info in Firebase
            val setUserInfo = FireConstants.usersRef.child(FireManager.uid).updateChildrenRx(userDict).toObservable<Any>()


            return@flatMap Observable.merge(arrayListOf(fetchGroups, fetchBroadcasts, setUserInfo))

        }
    }

    private fun getUserImage(): Observable<Pair<String, String>> {

        return FireConstants.usersRef.child(FireManager.uid)
                .observeSingleValueEvent().toObservable()
                .map { snapshot ->

                    val photoUrl = snapshot.child("photo").value as? String?
                    val thumb = snapshot.child("thumbImg").value as? String?

                    if (photoUrl != null && thumb != null) {
                        currentUserPhotoUrl = photoUrl
                        currentUserPhotoThumb = thumb
                        return@map Pair(photoUrl, thumb)
                    } else {
                        return@map Pair("", "")
                    }
                }
    }


    private fun completeSetupWithPickedImage(imagePath: String, userName: String): Observable<Any> {
        return fireManager.updateMyPhoto(imagePath).toObservable().flatMap {

            val thumb = it.first
            val localPhotoPath = it.second
            val photoUrl = it.third

            val userInfo = getUserInfoHashmap(userName, thumb, photoUrl, localPhotoPath)

            //save user info locally


            localPhotoPath?.let { it1 -> saveUserInfo(it1, thumb, userName) }


            //save user info in Firebase
            return@flatMap FireConstants.usersRef.child(FireManager.uid).updateChildrenRx(userInfo).toObservable<Any>()

        }.flatMap { ref ->
            //fetch previous groups if exists
            val fetchGroups = groupManager.fetchUserGroups()
            //fetch previous broadcasts if exists
            val fetchBroadcasts = broadcastManager.fetchBroadcasts(FireManager.uid)
            //combine both observables and execute them

            return@flatMap Observable.merge(fetchGroups, fetchBroadcasts)
        }
    }

    //this will fetch the 'defaultUserProfilePhoto' on the server
//it will be called if this user did not choose an image and he does not have a previous image on the server
    private fun getDefaultUserProfilePhoto(): Observable<Triple<String, String, String>> {

        return FireConstants.mainRef.child("defaultUserProfilePhoto").observeSingleValueEvent().toObservable().flatMap { snap ->
            val imgUrl = snap.value as? String?
            if (imgUrl != null) {

                _loadUserImage.value = imgUrl
                val filePath = DirManager.generateUserProfileImage()

                return@flatMap FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl).getFileRx(filePath).toObservable().map { Pair(filePath, imgUrl) }
            } else {
                return@flatMap Observable.error<Pair<File, String>>(NoDefaultImageException())
            }

        }.map { pair ->


            val filePath = pair.first.path
            val imgUrl = pair.second


            val circleBitmap = BitmapUtils.getCircleBitmap(BitmapUtils.convertFileImageToBitmap(filePath))
            val thumbImg = BitmapUtils.decodeImageAsPng(circleBitmap)
            currentUserPhotoThumb = thumbImg

            return@map Triple(filePath, imgUrl, thumbImg)


        }
    }


}