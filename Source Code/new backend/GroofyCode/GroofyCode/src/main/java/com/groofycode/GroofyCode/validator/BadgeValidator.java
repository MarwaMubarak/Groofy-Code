package com.groofycode.GroofyCode.validator;

import com.groofycode.GroofyCode.model.BadgeModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BadgeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BadgeModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BadgeModel badgeModel = (BadgeModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "Name is required");
        if (badgeModel.getName() != null && (badgeModel.getName().length() < 4 || badgeModel.getName().length() > 100)) {
            errors.rejectValue("name", "field.size", "Name must be between 4 and 100 characters");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "photo", "field.required", "Photo is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "field.required", "Description is required");
        if (badgeModel.getDescription() != null && (badgeModel.getDescription().length() < 4 || badgeModel.getDescription().length() > 1000)) {
            errors.rejectValue("description", "field.size", "Description must be between 4 and 1000 characters");
        }

        // You can add more validation logic as needed
    }
}
