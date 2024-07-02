package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewCertificateRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {
    @Id
    @ManyToOne
    private User user;
    @Id
    @GeneratedValue
    private long id;
    private String title;
    @ManyToOne
    private Institute institute;
    private LocalDate issuedDate;
    private LocalDate expiresDate;
    private String validityCheck;
    private String website;
    @ManyToMany
    private ArrayList<Skill> skills;

    public Certificate(NewCertificateRequest ncr, User u, ProxiesRepository pr) {
        this.user = u;
        this.title = ncr.getTitle();
        this.institute = pr.getProxy(Institute.class, ncr.getInstitute());
        this.issuedDate = ncr.getIssuedDate();
        this.expiresDate = ncr.getExpiresDate();
        this.validityCheck = ncr.getValidityCheck();
        this.website = ncr.getWebsite();
        this.skills = new ArrayList<>();
        for (long i : ncr.getSkills())
            this.skills.add(pr.getProxy(Skill.class, i));
    }
}
