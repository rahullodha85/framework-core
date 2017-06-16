package com.hbcd.scripting.core;

import com.hbcd.utility.entity.ActionParameters;

/**
 * Created by ephung on 3/14/2016.
 */
public class ApplyObjectAction_DragDrop_Level1 extends  BaseApplyObjectAction {
    String _frameFromNameOrId = "";
    public ApplyObjectAction_DragDrop_Level1(ActionParameters acp) {
        super(acp);
    }

    public ApplyObjectAction_DragDrop_Level1(ActionParameters acp, String frameNameOrId)
    {
        super(acp);
        _frameFromNameOrId = frameNameOrId;
    }

    public ApplyObjectAction_DragDrop_Level2 dragToTargetFrame(String frameToNameOrId) throws Exception {
        try {
            return new ApplyObjectAction_DragDrop_Level2(_frameFromNameOrId, _parameters, frameToNameOrId);
        } catch (Exception e) {
            throw e;
        }
    }
}
