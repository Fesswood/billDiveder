package com.vk.fesswood.billdivider.ui.activity

import android.animation.Animator
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.vk.fesswood.billdivider.App.App
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.interaction.SumInteractionListener
import com.vk.fesswood.billdivider.data.model.SumPart
import com.vk.fesswood.billdivider.ui.fragment.BaseRecyclerViewFragment
import com.vk.fesswood.billdivider.ui.fragment.BillFragment
import com.vk.fesswood.billdivider.ui.fragment.UserFragment
import com.vk.fesswood.billdivider.utils.GUIUtils
import io.realm.Realm
import kotlinx.android.synthetic.activity_main.*
import java.io.IOException
import java.util.*


public class MainActivity : BaseActivity()
                        , View.OnClickListener
                        , ViewPager.OnPageChangeListener
                        , BaseRecyclerViewFragment.SumAdapterChangeListener{

    private final  val TAG: String = MainActivity::class.simpleName as String

    private var adapter  = Adapter(supportFragmentManager)
    public final val PICK_CONTACT= 2015;
    private var userAddListener: OnContactAddListener? = null;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager);
        fabAddMore.setOnClickListener(this)
        setSumInTitle()
    }

    private fun setupViewPager(viewPager: ViewPager) {

        adapter.addFragment(BillFragment(), resources.getString(R.string.bills))
        adapter.addFragment(UserFragment(), resources.getString(R.string.people))
        adapter.addFragment(BillFragment(), resources.getString(R.string.stats))
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
    }

    class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val mFragments = ArrayList<Fragment>()
        private val mFragmentTitles = ArrayList<String>()



        public fun addFragment(fragment: BaseRecyclerViewFragment, title: String) {
            mFragments.add(fragment)
            mFragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }

        override fun getCount(): Int {
            return mFragments.size()
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitles.get(position)
        }
    }

    override fun onClick(v: View?) {
        if(viewpager.currentItem == 0){
            GUIUtils.showDialog(main_content, this)
        }
        else{
              userAddListener!!.addContact()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        when (viewpager.currentItem){
            0 -> {
                GUIUtils.changeFub(fabAddMore,
                        this.resources.getDrawable(R.drawable.ic_add_white_24dp,this.theme))
            }
            1 -> {
                GUIUtils.changeFub(fabAddMore,
                        this.resources.getDrawable(R.drawable.ic_person_add_white_24dp,this.theme))
            }
            2 -> {
                GUIUtils.hideViewByScale(fabAddMore,null)
            }
        }
    }

    override fun DataChanged() {
        setSumInTitle()
    }

    private fun setSumInTitle() {
        val sum = getRealm().where(SumPart::class.java).sumDouble("value")
        supportActionBar.title = resources.getString(R.string.str_sum_toolbar) + " $sum р"
    }

    private fun showSnackbarForDeletedRow(part: SumPart){
        Snackbar.make(viewpager, R.string.row_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_action_undo, View.OnClickListener {
                    var realm =Realm.getDefaultInstance();
                    realm.executeTransaction {
                        realm.copyToRealmOrUpdate(part)
                    }
                })
                .show()
        Log.d(TAG,"showSnackbarForDeletedRow")
    }

    override fun rowDeleted(deletedRow: SumPart) {
        Log.d(TAG,""+deletedRow.title + deletedRow.value)
        showSnackbarForDeletedRow(deletedRow)
    }



     public interface OnContactAddListener{
        fun addContact()
    }

    public fun setUserAddListener(listener : OnContactAddListener){
        userAddListener = listener
    }
}
