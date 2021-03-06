/*
 * Author: Madhur Ahuja
 * Copyright (c) 2014.
 */

package in.co.madhur.vocabbuilder.ui;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import in.co.madhur.vocabbuilder.App;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 28-Jun-14.
 */
public class SortActionProvider extends ActionProvider implements PopupMenu.OnMenuItemClickListener
{
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    private Context context;

    public SortActionProvider(Context context)
    {
        super(context);
        this.context=context;
    }

    @Override
    public View onCreateActionView()
    {


        return null;
    }

    @Override
    public boolean hasSubMenu()
    {
        return true;
    }

    @Override
    public boolean onPerformDefaultAction()
    {
        return super.onPerformDefaultAction();
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu)
    {

        subMenu.clear();
        subMenu.add(0, R.id.action_sortalpha_asc, 0, R.string.action_sortalpha_asc);
        subMenu.add(0, R.id.action_sortalpha_desc, 0, R.string.action_sortalpha_desc);
        subMenu.add(0, R.id.action_sortstar_asc, 0, R.string.action_sortstar_asc);
        subMenu.add(0, R.id.action_sortstar_desc, 0, R.string.action_sortstar_desc);
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {

        return true;
    }
}
