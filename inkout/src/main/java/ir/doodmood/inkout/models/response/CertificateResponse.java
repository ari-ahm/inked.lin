package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Certificate;
import ir.doodmood.inkout.models.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {
    private long user;
    private long id;
    private String title;
    private long institute;
    private String issuedDate;
    private String expiresDate;
    private String validityCheck;
    private String website;
    private Set<Long> skills;

    public CertificateResponse(Certificate certificate) {
        this.user = certificate.getUser().getId();
        this.id = certificate.getId();
        this.title = certificate.getTitle();
        this.institute = certificate.getInstitute().getId();
        this.issuedDate = certificate.getIssuedDate().toString();
        if (certificate.getExpiresDate() != null)
            this.expiresDate = certificate.getExpiresDate().toString();
        this.validityCheck = certificate.getValidityCheck();
        this.website = certificate.getWebsite();
        this.skills = new HashSet<>();
        for (Skill skill : certificate.getSkills())
            this.skills.add(skill.getId());
    }
}
