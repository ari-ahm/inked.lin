package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewCertificateRequest;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Certificate {
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
    private Set<Skill> skills;

    public Certificate(NewCertificateRequest ncr, User u, ProxiesRepository pr) {
        this.user = u;
        this.title = ncr.getTitle();
        this.institute = pr.getProxy(Institute.class, ncr.getInstitute());
        this.issuedDate = LocalDate.parse(ncr.getIssuedDate());
        if (expiresDate != null)
            this.expiresDate = LocalDate.parse(ncr.getExpiresDate());
        this.validityCheck = ncr.getValidityCheck();
        this.website = ncr.getWebsite();
        this.skills = new HashSet<>();
        for (long i : ncr.getSkills())
            this.skills.add(pr.getProxy(Skill.class, i));
    }
}
