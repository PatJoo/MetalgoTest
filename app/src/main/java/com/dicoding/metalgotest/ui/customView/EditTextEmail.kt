package com.dicoding.metalgotest.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.bumptech.glide.Glide.init
import com.google.android.material.textfield.TextInputEditText

class EditTextEmail : TextInputEditText {

        constructor(context: Context) : super(context) {
        init()
    }

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
        ) {
        init()
    }

        private fun init() {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    // Do nothing.
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                        error = "Wrong email format!"
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    // Do nothing.
                }
            })
        }
    }