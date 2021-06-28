package com.winkbr.browser.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.webkit.CookieManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.winkbr.browser.R
import kotlinx.android.synthetic.main.settings_activity.*
import java.io.File


class SettingsActivity : AppCompatActivity() {
    private lateinit var preferenceProvider: PreferenceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferenceProvider = PreferenceProvider(applicationContext)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.prefs, rootKey)
            
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference!!.key) {
                "default_browser" -> {
                    return try {
                        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                        requireContext().startActivity(intent)
                        true
                    } catch (e: ActivityNotFoundException) {
                        view?.snack("Error")
                        false
                    }
                }
                "password" -> {
                    view?.snack("Restart required.")
                    return true
                }
                "location" -> {
                    view?.snack("Restart required.")
                    return true
                }
                "load_image" -> {
                    view?.snack("Restart required.")
                    return true
                }
                "accept_cookies" -> {
                    view?.snack("Restart required.")
                    return true
                }
                "form_data" -> {
                    view?.snack("Restart required.")
                    return true
                }
                "clear_cookies" -> {
                    clearCookies()
                    return true
                }
                "clear_cache" -> {
                    clearAppCache()
                    return true
                }
                "faq" -> {
                    val intent = Intent(activity, TabActivity::class.java)
                    intent.putExtra("query", "http://winkbr.com/faq")
                    startActivity(intent)
                    return true
                }
                "about" -> {
                    val intent = Intent(activity, TabActivity::class.java)
                    intent.putExtra("query", "http://winkbr.com/about")
                    startActivity(intent)
                    return true
                }

            }
            return super.onPreferenceTreeClick(preference)
        }


        private fun clearCookies() {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
            view?.snack("Cookies Cleared")
        }
        private fun clearAppCache() {
            val cache = context?.cacheDir
            val appDir = File(cache?.parent)
            if (appDir.exists()) {
                val children = appDir.list()
                for (s in children) {
                    if (s != "lib") {
                        deleteDir(File(appDir, s))
                    }
                }
            }
            view?.snack("Cache Cleared")
        }

        private fun deleteDir(dir: File?): Boolean {
            if (dir != null && dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    val success = deleteDir(File(dir, children[i]))
                    if (!success) {
                        return false
                    }
                }
            }
            return dir!!.delete()
        }


        private fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(this, message, duration).show()
        }
        private fun sendEmail() {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "support@winkbr.com", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }



    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}