package com.codeit.hobbyzone.auth.presentation.dto.request;

import jakarta.validation.constraints.Email;

public record SendMailDto(@Email String email) {
}
