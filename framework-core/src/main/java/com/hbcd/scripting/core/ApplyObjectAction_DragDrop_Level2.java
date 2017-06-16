package com.hbcd.scripting.core;

import com.hbcd.common.utility.ObjectPropertyUtility;
import com.hbcd.core.genericfunctions.GenericFunctions;
import com.hbcd.utility.entity.ActionParameters;
import com.hbcd.utility.entity.ObjectSearchParameters;

/**
 * Created by ephung on 3/15/2016.
 */
public class ApplyObjectAction_DragDrop_Level2 extends  BaseApplyObjectAction {
    String _frameFromNameOrId = "";
    String _frameToNameOrId = "";
    public ApplyObjectAction_DragDrop_Level2(ActionParameters acp) {
        super(acp);
    }

    public ApplyObjectAction_DragDrop_Level2(String frameFromNameOrId, ActionParameters acp, String frameToNameOrId)
    {
        super(acp);
        _frameFromNameOrId = frameFromNameOrId;
        _frameToNameOrId = frameToNameOrId;
    }

    public void releaseOn(String objName) throws Exception {
        try {
            ActionParameters destination = new ActionParameters();
            destination.add(new ObjectSearchParameters().setType(1).setObjectRepository(ObjectPropertyUtility.getObjectFromService(objName)));
            GenericFunctions.coreDragDrop2(_frameFromNameOrId, _parameters, _frameToNameOrId, destination);
        } catch (Exception e) {
            throw e;
        }
    }
}
