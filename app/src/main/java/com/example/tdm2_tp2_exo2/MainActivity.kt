package com.example.tdm2_tp2_exo2

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tdm2_tp2_exo2.contact.Contact
import com.example.tdm2_tp2_exo2.contact.ContactAdapter
import com.example.tdm2_tp2_exo2.email.JavaMailAPI
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val PERMISSIONS_REQUEST_READ_CONTACTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contact_List.layoutManager = LinearLayoutManager(this)

        send.setOnClickListener{
            println("sending email ")
                senEmail()

        }

        addContact.setOnClickListener{
            val contactList: MutableList<Contact> = ArrayList()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                requestContactPermission()


            }
            val contacts= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (contacts!!.moveToNext()){
                val name= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val num= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val email= contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID
                ))

                 val obj= Contact()
                obj.name=name
                obj.num=num
                obj.email=email

                contactList.add(obj)
            }
            contact_List.adapter =
                ContactAdapter(
                    contactList,
                    this
                )
            contacts.close()
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


    private fun senEmail() {
        val mEmail: String = "gh_ishakboushaki@esi.dz"
        val mSubject: String = "from contact app"
        val mMessage: String = "the mail sending works!"
        val javaMailAPI = JavaMailAPI(
            this,
            mEmail,
            mSubject,
            mMessage
        )
        javaMailAPI.execute()
    }
}
