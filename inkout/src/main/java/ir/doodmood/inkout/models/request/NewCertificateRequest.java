package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.util.RegexPatterns;
import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class NewCertificateRequest extends BasicRequest {
    private String title;
    private Long institute;
    private String issuedDate;
    @Optional
    private String expiresDate;
    private String validityCheck;
    private String website;
    private Set<Long> skills;

    public boolean validate() {
        if (title.length() > 40 ||
            validityCheck.length() > 40 ||
            website.length() > 40 ||
            skills.size() > 5 ||
            !RegexPatterns.getWebsitePattern().matcher(getWebsite()).matches())
            return false;
        return true;
    }
}
