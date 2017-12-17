package com.arranlomas.mvisample.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arranlomas.kontent.commons.KontentActivity
import com.arranlomas.mvisample.R
import com.arranlomas.mvisample.repository.AuthApi
import com.arranlomas.mvisample.repository.AuthRepository
import com.arranlomas.mvisample.signup.objects.SignupIntent
import com.arranlomas.mvisample.signup.objects.SignupViewState
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * Created by arran on 4/12/2017.
 */
internal class SignupFragment : KontentActivity<SignupViewState, SignupIntent>() {

    var interactor: SignupContract.Interactor

    init {
        //this step would preferbly be done via dagger ot dependency injects, and the interactor would just be initejected as an interface, this step is vital for testing
        val authApi = AuthApi()
        val authRepository = AuthRepository(authApi)
        this.interactor = SignupInteractor(authRepository)
        super.setup(interactor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        super.attachIntents(intents())
        loginButton.setOnClickListener {
            //Navigate to login screen
        }
    }

    fun signupButtonIntent(): Observable<SignupIntent> = RxView.clicks(signupButton)
            .map {
                SignupIntent.SignupCta(
                        emailEditText.text.toString(),
                        usernameEditText.text.toString(),
                        passwordEditText.text.toString())
            }


    fun intents(): Observable<SignupIntent> {
        return signupButtonIntent()
    }

    override fun render(state: SignupViewState) {
        emailError.setVisible(state.emailError != null)
        state.emailError?.let { emailError.text = getText(it) }

        usernameError.setVisible(state.usernameError != null)
        state.usernameError?.let { usernameError.text = getText(it) }

        passwordError.setVisible(state.passwordError != null)
        state.passwordError?.let { passwordError.text = getText(it) }

        state.networkError?.let {
            val toast = Toast(this)
            toast.setText(it.localizedMessage)
            toast.show()
        }

        signupButton.setVisible(!state.loading)
        loading.setVisible(state.loading)

        if (state.isLoggedIn) {
            //Navigate to logged in state
        }
    }

    fun View.setVisible(visible: Boolean) {
        if (visible) this.visibility = View.VISIBLE
        else this.visibility = View.GONE
    }
}
