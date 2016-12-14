package com.example.swapn.bookmyclass;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swapn.bookmyclass.common.CircleTransform;
import com.example.swapn.bookmyclass.common.Constants;
import com.example.swapn.bookmyclass.common.Util;
import com.example.swapn.bookmyclass.fragments.BookDetailsFragment;
import com.example.swapn.bookmyclass.fragments.BookSellingInformationFragment;
import com.example.swapn.bookmyclass.fragments.BuyBookFragment;
import com.example.swapn.bookmyclass.fragments.ConversationsFragment;
import com.example.swapn.bookmyclass.fragments.CourseDetailFragment;
import com.example.swapn.bookmyclass.fragments.CoursesMasterFragment;
import com.example.swapn.bookmyclass.fragments.EventMasterFragment;
import com.example.swapn.bookmyclass.fragments.InstructorsFragment;
import com.example.swapn.bookmyclass.fragments.MainFragment;
import com.example.swapn.bookmyclass.fragments.MapFragment;
import com.example.swapn.bookmyclass.fragments.MessageFragment;
import com.example.swapn.bookmyclass.fragments.SellBookFragment;
import com.example.swapn.bookmyclass.fragments.SellBookMasterFragment;
import com.example.swapn.bookmyclass.fragments.SelllingBooksFragment;
import com.example.swapn.bookmyclass.fragments.StudyGroupMessagesFragment;
import com.example.swapn.bookmyclass.fragments.ViewSellersFragment;
import com.example.swapn.bookmyclass.models.Book;
import com.example.swapn.bookmyclass.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MainActivity extends AppCompatActivity implements OnConnectionFailedListener,MainFragment.OnFragmentInteractionListener ,
 SelllingBooksFragment.OnFragmentInteractionListener, BookDetailsFragment.OnFragmentInteractionListener, BuyBookFragment.OnFragmentInteractionListener,
        SellBookMasterFragment.OnFragmentInteractionListener, CoursesMasterFragment.OnFragmentInteractionListener, CourseDetailFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener, ViewSellersFragment.OnFragmentInteractionListener, MessageFragment.OnFragmentInteractionListener, ConversationsFragment.OnFragmentInteractionListener,
        InstructorsFragment.OnFragmentInteractionListener, EventMasterFragment.OnFragmentInteractionListener, StudyGroupMessagesFragment.OnFragmentInteractionListener
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Util u = Util.getInstance();
    private Toolbar toolbar;
    User userData;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        userData = u.getUserFromSHaredPreference(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayShowHomeEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.log_off :
                        mAuth.signOut();
                        u.setSharedPreferences(getApplicationContext(),null);
                        openLoginActivity();
                        break;
                    case R.id.sell_book :
                        //openSellBookFragment();
                        openSellBookMasterFragment();
                        break;
                    case R.id.selling_books :
                        openSellingBookFragment();
                        break;
                    default:
                        //Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        View header = navigationView.getHeaderView(0);
      /*  header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileDetailFragment();
            }
        }); */

        ImageView profpic = (ImageView) header.findViewById(R.id.profile_image);
        TextView username = (TextView) header.findViewById(R.id.username);
        TextView useremail = (TextView) header.findViewById(R.id.email);
        username.setText(userData.getName());
        useremail.setText(userData.getEmail());
        if(!userData.getProf_pic().equalsIgnoreCase("default"))
            Picasso.with(MainActivity.this).load(Uri.parse(userData.getProf_pic())).transform(new CircleTransform()).into(profpic);
        else
            Picasso.with(MainActivity.this).load(R.drawable.anonymus).transform(new CircleTransform()).into(profpic);
        //  Picasso.with(RecordWorkOutActivity.this).load(Uri.parse(userData.getProf_pic())).fit().centerCrop().into(profpic);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        initialiseApp();
    }

    public void openStudyGroupMessageFragment(String group_id, String group_name) {
        drawerLayout.closeDrawers();
        StudyGroupMessagesFragment fragment = StudyGroupMessagesFragment.newInstance(group_id);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle(group_name);
    }

    public void openSellerMessageFragment(String seller_id, String book_name, String buyer_name) {
        drawerLayout.closeDrawers();
        MessageFragment fragment = MessageFragment.newInstance(seller_id, book_name);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle(buyer_name);
    }

    public void openViewSellerFragment(String book_id) {
        drawerLayout.closeDrawers();
        ViewSellersFragment fragment = ViewSellersFragment.newInstance(book_id);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Sellers");
    }

    public void openBookSellingInformationFragment() {
        drawerLayout.closeDrawers();
        BookSellingInformationFragment fragment = new BookSellingInformationFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        Log.e("Google", "Failed To Connect to Google Apis");
        // ...
    }

    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    public void openMapFragment (String location) {
        drawerLayout.closeDrawers();
        MapFragment fragment = MapFragment.newInstance(location);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    public void openAllCoursesFragment () {
        drawerLayout.closeDrawers();
        CoursesMasterFragment fragment = new CoursesMasterFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Courses");
    }

    public void openConversationFragment () {
        drawerLayout.closeDrawers();
        ConversationsFragment fragment = new ConversationsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Chats");
    }

    public void openCourseDetailsFragment (String course_id) {
        drawerLayout.closeDrawers();
        CourseDetailFragment fragment = CourseDetailFragment.newInstance(course_id);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle(course_id);
    }

    public void openUrl(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openSellBookFragment () {
        drawerLayout.closeDrawers();
        SellBookFragment fragment = new SellBookFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    public void BookDetailsFragment (String book_id, String book_name) {
        drawerLayout.closeDrawers();
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BOOKID, book_id);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle(book_name);
    }

    public void BuyBookFragment (String book_id) {
        drawerLayout.closeDrawers();
        BuyBookFragment fragment = new BuyBookFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BOOKID, book_id);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    public void CourseDetailsFragment (String course_id) {
        drawerLayout.closeDrawers();
        // #TODO Change to course details fragment
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.COURSEID, course_id);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    public void openSellBookMasterFragment () {
        drawerLayout.closeDrawers();
        SellBookMasterFragment fragment = new SellBookMasterFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    public void openSellingBookFragment () {
        drawerLayout.closeDrawers();
        SelllingBooksFragment fragment = new SelllingBooksFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BOOKSDETAILTYPE, Constants.BOOKS_TYPE_SELLING);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Selling Books");
    }

    public void openAllBookFragment () {
        drawerLayout.closeDrawers();
        SelllingBooksFragment fragment = new SelllingBooksFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BOOKSDETAILTYPE, Constants.BOOKS_TYPE_ALL);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Books");
    }

    public void openEventsFragment() {
        drawerLayout.closeDrawers();
        EventMasterFragment fragment = new EventMasterFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Events");
    }

    public void openInstructorFragment() {
        drawerLayout.closeDrawers();
        InstructorsFragment fragment = new InstructorsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("Professors");
    }

    public void openMainFragment() {
        drawerLayout.closeDrawers();
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
        setTitle("BookMyClass");
    }

    public void initialiseApp() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            openSellingBookFragment();
            Toast.makeText(this,"Landscape",Toast.LENGTH_LONG);
        }
        else {
            openMainFragment();
            // Portrait
        }
    }

    public void openLoginActivity () {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                Toast.makeText(this, "setting clicked", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_message:
                Toast.makeText(this, "Message Clicked", Toast.LENGTH_SHORT)
                        .show();
                openConversationFragment();
                break;
            case R.id.action_home:
                initialiseApp();
                break;
            default:
                initialiseApp();
                break;
        }

        return true;
    }

    @Override
    public User getUserData() {
        return userData;
    }


}
