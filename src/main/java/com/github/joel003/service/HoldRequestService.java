package com.github.joel003.service;

import com.github.joel003.dao.HoldRequestDAO;
import com.github.joel003.entity.HoldRequest;

import java.sql.SQLException;
import java.util.List;

public class HoldRequestService {
    private final HoldRequestDAO holdRequestDAO;
    public HoldRequestService()
    {
        this.holdRequestDAO = new HoldRequestDAO();
    }

    public boolean addHoldRequest(HoldRequest holdRequest) {
        try {
            return holdRequestDAO.addHoldRequest(holdRequest);
        }catch (SQLException e){
            System.out.println("SQLException (Add-HoldRequest): "+e.getMessage());
            return false;
        }
    }

    public List<HoldRequest> viewHoldRequests() {
        try {
            return holdRequestDAO.viewHoldRequests();
        }catch (SQLException e){
            System.out.println("SQLException (View-HoldRequests): "+e.getMessage());
            return null;
        }
    }
}
