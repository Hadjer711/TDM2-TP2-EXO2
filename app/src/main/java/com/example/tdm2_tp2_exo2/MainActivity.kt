package com.example.tdm2_tp2_exo2

import android.Manifest
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tdm2_tp2_exo2.broadcastService.SmsReceiver
import com.example.tdm2_tp2_exo2.contact.Contact
import com.example.tdm2_tp2_exo2.contact.ContactAdapter
import com.example.tdm2_tp2_exo2.contact.ContactAdapter.OnContactListener
import com.example.tdm2_tp2_exo2.email.JavaMailAPI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , OnContactListener{
    private val appPermission = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_MMS)

    val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    val PERMISSIONS_REQUEST_READ_SMS=1
    val PERMISSIONS_REQUEST_RECEIVE_SMS=1
    val contactList: MutableList<Contact> = ArrayList()
    var selectedContact : ArrayList<Contact> = ArrayList()

    private lateinit var contactAdapter: ContactAdapter

    private var instance: MainActivity? = null

    fun getInstance(): MainActivity? {
        return instance
    }

    fun getContext(): Context? {
        return instance
        // or return instance.getApplicationContext();
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        this.instance = this;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECEIVE_SMS),
                    PERMISSIONS_REQUEST_RECEIVE_SMS)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        var receiver: SmsReceiver = SmsReceiver()

        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter)
        initRecycleView()







        addContact.setOnClickListener{

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                requestContactPermission()


            }
            val contacts= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (contacts!!.moveToNext()){
                val name= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val num= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val email= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.SEND_TO_VOICEMAIL
                ))

                 val obj= Contact()
                obj.name=name
                obj.num=num
                obj.email=email

                contactList.add(obj)
            }
            initRecycleView()


            contacts.close()
        }
    }

    private fun initRecycleView(){
        contact_List. apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            val topSpacingItemDecoration= TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            contactAdapter= ContactAdapter(this@MainActivity, contactList)
            adapter= contactAdapter
        }

    }


    fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Read Contacts permission")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener(DialogInterface.OnDismissListener {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_SMS),
                            PERMISSIONS_REQUEST_READ_SMS)

                        requestPermissions(
                            arrayOf(Manifest.permission.RECEIVE_SMS),
                            PERMISSIONS_REQUEST_RECEIVE_SMS)

                    })
                    builder.show()
                } else {
                    requestPermissions(
                        this, arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            }
        }
    }



     final fun context():MainActivity{
        return this
    }

    override fun onContactClick(contact: Contact, position: Int) {
        Log.d("contact", "cliick")
        if(contact.select){
            contact.select=false
        }
        else{
            contact.select=true
        }

        contactAdapter.notifyDataSetChanged()

    }


}
