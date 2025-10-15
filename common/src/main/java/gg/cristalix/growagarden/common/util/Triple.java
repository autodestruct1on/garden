package gg.cristalix.growagarden.common.util;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Triple<A, B, C> {

  private A first;
  private B second;
  private C third;

}