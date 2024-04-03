package com.izabarovsky.callsign.telegram.bot.tg.dialog;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DialogStateService {

    private final Map<Long, DialogState> userStateMap = new HashMap<>();

    public DialogState getState(Long id) {
        return userStateMap.get(id);
    }

    public DialogState putState(Long id, DialogState state) {
        return userStateMap.put(id, state);
    }

    public void dropState(Long id) {
        userStateMap.remove(id);
    }

}
