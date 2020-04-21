package com.gistMED.gistmd.Classes;

import java.util.ArrayList;

public class Organization {

    private String orgName;
    private String pointer;
    private String ID;
    private ArrayList<Organization> subOrg = new ArrayList<>();

    public Organization(String orgName, String pointer, String ID) {
        this.orgName = orgName;
        this.pointer = pointer;
        this.ID = ID;
    }

    public void AddSuborganization(Organization org)
    {
        this.subOrg.add(org);
    }

    public static ArrayList<String> GetOrgsNamesArryList()
    {
        ArrayList<String> orgsNames = new ArrayList<>();

        for(Organization organization:StaticObjects.Organizations)
        {
            for (Organization suborg:organization.subOrg)
            {
                orgsNames.add(organization.orgName + " " + suborg.orgName);
            }
        }
        return orgsNames;
    }
}
