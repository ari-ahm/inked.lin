package ir.doodmood.inkout.models.request;

import ir.doodmood.inkout.util.RegexPatterns;
import ir.doodmood.mashtframework.annotation.gson.AllRequired;
import ir.doodmood.mashtframework.annotation.gson.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AllRequired
public class NewCertificateRequest extends BasicRequest {
    private String title;
    private Long institute;
    private LocalDate issuedDate;
    @Optional
    private LocalDate expiresDate;
    private String validityCheck;
    private String website;
    private ArrayList<Long> skills;

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
