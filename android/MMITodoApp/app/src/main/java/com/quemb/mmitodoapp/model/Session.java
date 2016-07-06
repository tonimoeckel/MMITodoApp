package com.quemb.mmitodoapp.model;


import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.Date;

/**
 * Created by tonimockel on 06.07.16.
 */

@Table
public class Session extends SugarRecord {

    public boolean authenticated;
    public Date loginDate;

}
