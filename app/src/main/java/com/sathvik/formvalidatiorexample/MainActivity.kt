package com.sathvik.formvalidatiorexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), com.mobsandgeeks.saripaar.Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, messageResId = R.string.txt_validation_required)
    @BindView(R.id.phonenumber_edit_text)
    lateinit var phoneNumberEditText: EditText

    @Order(2)
    @NotEmpty(sequence = 1, messageResId = R.string.txt_validation_required)
    @Password(sequence = 2, min = 6, messageResId = R.string.txt_password_validation_failed)
    @Length(sequence = 3, max = 30, messageResId = R.string.txt_password_validation_failed)
    @BindView(R.id.password_edit_text)
    lateinit var passwordEditText: EditText

    @Order(3)
    @NotEmpty(sequence = 1, messageResId = R.string.txt_validation_required)
    @ConfirmPassword(sequence = 2, messageResId = R.string.txt_confirm_password_validation_failed)
    @BindView(R.id.confirm_password_edit_text)
    lateinit var confirmPasswordEditText: EditText


    private lateinit var validator: Validator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        validator = Validator(this)
        validator.setValidationListener(this)

    }

    @OnClick(R.id.create_account)
    fun onSubmit() {
        validator.validate()
    }

    override fun onValidationSucceeded() {
        Toast.makeText(this, "Validation successful", Toast.LENGTH_SHORT).show()
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        if (errors != null && errors.size > 0) {
            val view = errors[0].view as TextView
            val failedRules = errors[0].failedRules
            if (failedRules != null && failedRules.size > 0) {
                val message = failedRules[0].getMessage(this)
                updateErrorMessages(view, message)

            }
        }
    }

    private fun updateErrorMessages(newFailedView: TextView, failureMessage: String) {
        for (view in listOf<TextView>(phoneNumberEditText, passwordEditText, confirmPasswordEditText)) {
            view.error = null
        }
        if (newFailedView is EditText) {
            newFailedView.requestFocus()
            newFailedView.setError(failureMessage)
        } else {
            Toast.makeText(this, failureMessage, Toast.LENGTH_LONG).show()
        }
    }
}
