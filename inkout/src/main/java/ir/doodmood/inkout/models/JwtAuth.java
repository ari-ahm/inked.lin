package ir.doodmood.inkout.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class JwtAuth {
    private Long id;
}
