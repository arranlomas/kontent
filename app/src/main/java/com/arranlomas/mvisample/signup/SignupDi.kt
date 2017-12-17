package com.tempomatch.tempoauth.signup

import com.arranlomas.mvisample.signup.SignupContract
import com.arranlomas.mvisample.signup.SignupFragment
import com.arranlomas.mvisample.signup.SignupInteractor
import com.tempomatch.core.auth.IAuthRepository
import com.tempomatch.core.di.RepositoryComponent
import com.tempomatch.tempocomponent.commons.PresenterScope
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 5/12/2017.
 */
@PresenterScope
@Component(modules = arrayOf(SignupModule::class), dependencies = arrayOf(RepositoryComponent::class))
internal interface SignupComponent {
    fun inject(signupFragment: SignupFragment)
}

@Module
internal class SignupModule {

    @Provides
    @PresenterScope
    internal fun providesSettingsPresenter(authRepository: IAuthRepository): SignupContract.Interactor {
        return SignupInteractor(authRepository)
    }


}
