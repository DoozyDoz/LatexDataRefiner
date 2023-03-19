package com.example.latexdatarefiner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class LatexDetailsActivity : AppCompatActivity() {
    private var view_pager: ViewPager? = null
    private var viewPagerAdapter: SectionsPagerAdapter? = null
    private var tab_layout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs_icon)
        initComponent()
    }


    private fun initComponent() {
        view_pager = findViewById<View>(R.id.view_pager) as ViewPager
        tab_layout = findViewById<View>(R.id.tab_layout) as TabLayout
        setupViewPager(view_pager)
        tab_layout!!.setupWithViewPager(view_pager)
        for (i in tabIcon.indices) {
            val view: View = layoutInflater.inflate(R.layout.custom_tab, null)
            val tab = tab_layout!!.getTabAt(i)
            view.findViewById<View>(R.id.icon).setBackgroundResource(tabIcon[i])
            if (tab != null) tab.customView = view
        }
        tab_layout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        viewPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPagerAdapter!!.addFragment(KatexFragment.newInstance(), "Katex") // index 0
        viewPagerAdapter!!.addFragment(LatexFragment.newInstance(), "Latex") // index 1
        viewPager!!.adapter = viewPagerAdapter
    }

    private inner class SectionsPagerAdapter(manager: FragmentManager?) :
        FragmentPagerAdapter(manager!!) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        fun getTitle(position: Int): String {
            return mFragmentTitleList[position]
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    companion object {
        var sQuestionId: String? = null
        var sImage: String? = null
        val tabIcon = intArrayOf(R.drawable.ic_katex, R.drawable.ic_latex)
    }
}