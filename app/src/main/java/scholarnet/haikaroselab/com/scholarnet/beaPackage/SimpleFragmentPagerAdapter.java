package scholarnet.haikaroselab.com.scholarnet.beaPackage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by root on 4/24/16.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "ARTICLES", "MODULES"};
    private Context context;
    private String subjectId;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context,String subjectId) {
        super(fm);
        this.context = context;
        this.subjectId=subjectId;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return PostsFragment.newInstance("","");
        }
            return ModulesListFragment.newInstance(subjectId, "");
       }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}