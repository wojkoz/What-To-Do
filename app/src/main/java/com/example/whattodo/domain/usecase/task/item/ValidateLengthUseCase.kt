package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.R
import com.example.whattodo.utils.UiText

class ValidateLengthUseCase {
    operator fun invoke(text: String, maxLength: Int, minLength: Int = 1): UseCaseResult<Nothing> {
        if (text.length < minLength){
            return UseCaseResult(
                isSuccess = false,
                data = null,
                errorMessage = UiText.StringResource(R.string.this_field_is_required)
            )
        }

        if (text.length > maxLength){
            return UseCaseResult(
                isSuccess = false,
                data = null,
                errorMessage = UiText.StringResource(R.string.too_many_chars)
            )
        }


        return UseCaseResult(
            isSuccess = true,
            data = null,
            errorMessage = null
        )
    }
}