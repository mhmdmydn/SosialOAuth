package id.ghodel.sosialoauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import id.ghodel.sosialoauth.R.*
import org.jetbrains.annotations.Nullable


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var tvResult: AppCompatTextView
    companion object {
        const val RC_SIGN_IN = 9001
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signBtn = findViewById<SignInButton>(R.id.google_button).setOnClickListener(this)
        val signOutBtn = findViewById<Button>(R.id.sign_out_button).setOnClickListener(this)
        tvResult = findViewById<AppCompatTextView>(R.id.tv_result)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onStart() {
        super.onStart()
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
        // [END on_start_sign_in]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
               GoogleSignIn.getSignedInAccountFromIntent(data).apply {
                    handleSignInResult(this)
                }
        }
    }

    // [START handleSignInResult]
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
    // [END handleSignInResult]


    // [START signIn]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signIn]


    // [START signOut]
    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
    }
    // [END signOut]


    // [START revokeAccess]
    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
    }
    // [END revokeAccess]

    private fun updateUI(@Nullable account: GoogleSignInAccount?) {
        if (account != null) {
            Toast.makeText(applicationContext, account.displayName ,Toast.LENGTH_SHORT).show()
            tvResult.text = account.displayName + '\n' +
                    account.email + '\n' + account.id + '\n' + account.photoUrl + '\n' + account.serverAuthCode
        } else {
            Toast.makeText(applicationContext, "signOut", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.google_button -> signIn()
            R.id.sign_out_button -> signOut()
        }
    }
}