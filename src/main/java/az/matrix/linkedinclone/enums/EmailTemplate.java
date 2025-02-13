package az.matrix.linkedinclone.enums;

import lombok.Getter;

@Getter
public enum EmailTemplate {

    PASSWORD_RESET("You have requested to reset your password\n",
            """
                    Hi {userName},
                    Click the link below to change your password
                    {resetUrl}
                    Ignore this email if you do remember your password,or you have not made the request."""),

    APPLICATION_SEND(
            "{userName}, your application was sent to {organizationName}",
            "Your application was sent to {organizationName}"
    ),

    APPLICATION_VIEWED(
            "Your application was reviewed by {organizationName}",
            "Your application was reviewed by {organizationName}"
    ),

    APPLICATION_ACCEPTED(
            "Your application to {jobTitle} at {organizationName}",
            """
                    Dear {userName},
                    
                    We are pleased to inform you that your application for the {jobTitle} position at {organizationName} has been accepted.
                    Our team was impressed with your background and experience, and we would love to move forward with the next steps.
                    
                    Our team member will contact you soon to discuss further details.
                    
                    Looking forward to speaking with you!
                    
                    Best regards,
                    {organizationName}"""
    ),

    APPLICATION_REJECT(
            "Your application to {jobTitle} at {organizationName}",
            """
                    Dear {userName},
                    
                    Thanks for your interest in the {jobTitle} position at {organizationName}.
                    Unfortunately, we will not be moving forward with your application but we appreciate your time and interest in {organizationName}.
                    
                    Regards,
                    {organizationName}"""
    );
    private final String subject;
    private final String body;


    EmailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

}
