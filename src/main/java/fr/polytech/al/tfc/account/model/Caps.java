package fr.polytech.al.tfc.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Caps {
    private float money;
    private float amountSlidingWindow;
}
