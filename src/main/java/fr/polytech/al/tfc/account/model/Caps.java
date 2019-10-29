package fr.polytech.al.tfc.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Caps {
    @NonNull
    private Integer money;
    @NonNull
    private Integer amountSlidingWindow;
}
