package managment.protege.supermed.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CustomDivider;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import managment.protege.supermed.Fragment.AlternativeListFragment;
import managment.protege.supermed.Fragment.Cart;
import managment.protege.supermed.Fragment.Category;
import managment.protege.supermed.Fragment.ContactUsFragment;
import managment.protege.supermed.Fragment.HelpCenterFragment;
import managment.protege.supermed.Fragment.JoinUs;
import managment.protege.supermed.Fragment.OrderHistory;
import managment.protege.supermed.Fragment.SearchProductFragment;
import managment.protege.supermed.Fragment.Settings;
import managment.protege.supermed.Fragment.Home;
import managment.protege.supermed.Fragment.UploadPriscribtionFragment;
import managment.protege.supermed.Fragment.Wishlist_Fragment;
import managment.protege.supermed.Model.Banner;
import managment.protege.supermed.R;
import managment.protege.supermed.Tools.GlobalHelper;

import static managment.protege.supermed.Activity.Login.MY_PREFS_NAME;
import static managment.protege.supermed.Fragment.Home.ProductDetailCartCounter;

public class Main_Apps extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView cart, nobatch, wl_badge, nobatch_products;
    public static ImageView wishlist, search, search_toolbarapps, cart_toolbarapps;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    Toolbar toolbar;
    public static TextView toolbarTitle;
    static ImageView toolbarSearch, Wishlist;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    static Context context;
    private ActionBar mActionBar;
    static public String navigationHeaderUsername;
    static public TextView nav_username, nav_email;
    public static boolean status = true;
    static public CircleImageView nav_image;
    View Hview;
    public static KProgressHUD hud;

    public static List<Banner> myList;
    SharedPreferences prefs2;
    SharedPreferences prefs3;
    static public String images;
    boolean CheckerRegister = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__apps);
        initializations();

        myList = (ArrayList<Banner>) getIntent().getSerializableExtra("mylist");
        context = this;

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backfunction(new Cart());
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backfunction(new Wishlist_Fragment());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backfunction(new SearchProductFragment());
            }
        });

        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        drawerFunction(toolbar);        //drawer functionality
        NavigationFunction();           //navigation functionality

        if (GlobalHelper.getUserProfile(context) != null && GlobalHelper.getUserProfile(context).getProfile() != null) {
            if (GlobalHelper.getUserProfile(this).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                changeNavigationMenuItem("LOGIN");
            } else {
                changeNavigationMenuItem("LOGOUT");
            }
        }

        if (GlobalHelper.getUserProfile(context) != null && GlobalHelper.getUserProfile(context).getProfile() != null) {
            if (GlobalHelper.getUserProfile(this).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                nav_email.setVisibility(View.GONE);
            } else {
                nav_email.setVisibility(View.VISIBLE);
            }
        }
        prefs2 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        prefs3 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if (GlobalHelper.getUserProfile(context) != null && GlobalHelper.getUserProfile(context).getProfile() != null) {
            if (GlobalHelper.getUserProfile(context).getProfile().getCartCounter() <= 0) {
                Main_Apps.nobatch.setVisibility(View.GONE);
            } else {
                Main_Apps.nobatch.setVisibility(View.VISIBLE);
            }
        }
        CheckHomeOrLogin();
    }

    public void CheckHomeOrLogin() {


        if (GlobalHelper.getUserProfile(getApplicationContext()) != null
                && GlobalHelper.getUserProfile(getApplicationContext()).getProfile() != null
                && GlobalHelper.getUserProfile(getApplicationContext()).getProfile().getFirstName() != null
                && GlobalHelper.getUserProfile(getApplicationContext()).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
            CheckerRegister = false;
            HomeFragment();
        } else if (prefs2.getBoolean("CheckBox", false) || (prefs3.getBoolean("ComingFromRegister", false))) {
            CheckerRegister = false;
            HomeFragment();
        } else {
            Intent intent = new Intent(Main_Apps.this, Login.class);
            startActivity(intent);
        }
    }

    public void initializations() {

        hud = KProgressHUD.create(Main_Apps.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cart = (TextView) findViewById(R.id.cart);
        nobatch = (TextView) findViewById(R.id.nobatch);
        wishlist = (ImageView) findViewById(R.id.wishlist);
        search = (ImageView) findViewById(R.id.search);
        search_toolbarapps = (ImageView) findViewById(R.id.search_toolbarapps);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_text);
        wl_badge = (TextView) findViewById(R.id.wl_badge);
        toolbarSearch = (ImageView) findViewById(R.id.search);
        Wishlist = (ImageView) findViewById(R.id.wishlist);
    }

    static public void hideToolBarItems(String Title) {
        toolbarSearch.setVisibility(View.INVISIBLE);
        cart.setVisibility(View.INVISIBLE);
        nobatch.setVisibility(View.GONE);
        wl_badge.setVisibility(View.GONE);
        Wishlist.setVisibility(View.INVISIBLE);
        toolbarTitle.setText(Title);
    }

    static public void showToolBarItems(String Title) {
        toolbarSearch.setVisibility(View.VISIBLE);
        cart.setVisibility(View.VISIBLE);
        nobatch.setVisibility(View.VISIBLE);

        Wishlist.setVisibility(View.VISIBLE);
        wl_badge.setVisibility(View.VISIBLE);
        toolbarTitle.setText(Title);
    }

    public void addToolbar(final Context context, String Title, View v) {
        Toolbar toolbar2 = (Toolbar) v.findViewById(R.id.toolbar123);
        TextView toolbartext = (TextView) v.findViewById(R.id.toolbar_text);
        toolbartext.setText(Title);
        toolbar2.setNavigationIcon(R.drawable.arrow);
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void addToolbarBack(final Context context, String Title, View v) {
        Toolbar toolbarBack = (Toolbar) v.findViewById(R.id.toolbarBack);
        TextView toolbartext = (TextView) v.findViewById(R.id.toolbar_text);
        toolbartext.setText(Title);
        toolbarBack.setNavigationIcon(R.drawable.arrow);
        toolbarBack.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void HomeFragment() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new Home()).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public void NavigationFunction() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Hview = navigationView.getHeaderView(0);
        nav_username = (TextView) Hview.findViewById(R.id.nav_username);
        nav_email = (TextView) Hview.findViewById(R.id.nav_email);
        nav_image = (CircleImageView) Hview.findViewById(R.id.nav_image);

        if (GlobalHelper.getUserProfile(context) != null && GlobalHelper.getUserProfile(context).getProfile() != null) {
            if (GlobalHelper.getUserProfile(context).getProfile().getImage() != null) {
                images = GlobalHelper.getUserProfile(context).getProfile().getImage();
                Picasso.with(context).load(images).resize(80, 80)
                        .placeholder(R.drawable.edit_profile_icon)
                        .into(nav_image);
            }
            if (GlobalHelper.getUserProfile(context).getProfile().getFirstName() != null && GlobalHelper.getUserProfile(context).getProfile().getLastName() != null) {
                navigationHeaderUsername = GlobalHelper.getUserProfile(context).getProfile().getFirstName() + " " +
                        GlobalHelper.getUserProfile(context).getProfile().getLastName();
                nav_username.setText(navigationHeaderUsername);
            }
            if (GlobalHelper.getUserProfile(context).getProfile().getEmail() != null) {
                nav_email.setText(GlobalHelper.getUserProfile(context).getProfile().getEmail());
            }
        }
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new CustomDivider(Main_Apps.this, CustomDivider.VERTICAL));

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void changeNavigationMenuItem(String text) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_logout);
        menuItem.setTitle(text);

        navigationView.setNavigationItemSelectedListener(this);
    }

    public void drawerFunction(Toolbar toolbar) {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
    }

    public void backfunction(Fragment newfragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newfragment).
                addToBackStack(newfragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public void backfunction2(Fragment newfragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newfragment).
                addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public void backfunction(Fragment newfragment, Bundle args) {
        newfragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newfragment).
                addToBackStack(newfragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    public void backfunctions(Fragment newfragment, Bundle args) {
        newfragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newfragment).
                addToBackStack(newfragment.getClass().getName()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main__apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cart) {
            backfunction(new Cart());
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_Categories) {
            backfunction(new Category());
        }
        if (id == R.id.nav_settings) {
            backfunction(new Settings());
        }

        if (id == R.id.nav_orderHistory) {
            if (GlobalHelper.getUserProfile(context).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {
                forgotPasswordDialog(context);
            } else {
                backfunction(new OrderHistory());
            }
        }
        if (id == R.id.nav_alternateMedicine) {

            backfunction(new AlternativeListFragment());
        }
        if (id == R.id.nav_helpCenter) {
            backfunction(new HelpCenterFragment());
        }

        if (id == R.id.nav_uploadPresciption) {
            backfunction(new UploadPriscribtionFragment());
        }
        if (id == R.id.nav_contact) {
            backfunction(new ContactUsFragment());
        }
        if (id == R.id.nav_join) {
            backfunction(new JoinUs());
        }
        if (id == R.id.nav_placeOrder) {
            backfunction(new Cart());
        }
        if (id == R.id.nav_logout) {

            if (GlobalHelper.getUserProfile(this).getProfile().getFirstName().trim().toLowerCase().equals("guest")) {

                removeData();

            } else {
                ViewDialog alert = new ViewDialog();
                alert.showDialog(Main_Apps.this, "Are you sure you want to logout from app?");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Main_Apps getMainActivity() {

        return (Main_Apps) context;
    }

    public class ViewDialog {

        public void showDialog(Activity activity, String msg) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogforlogout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.logout);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    removeData();
                }
            });
            Button dialogCancel = (Button) dialog.findViewById(R.id.cancel);
            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    private void removeData() {
        getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
        prefs2.edit().clear().apply();
        prefs3.edit().clear().apply();
        GlobalHelper.getUserProfile(getApplicationContext()).setProfile(null);
        GlobalHelper.getUserProfile(getApplicationContext()).getProfile().setFirstName("");

        GlobalHelper.getUserProfile(null);
        SharedPreferences preferences = getSharedPreferences(GlobalHelper.getUserProfile(getApplicationContext()).getProfile().getFirstName(), 0);
        preferences.edit().remove("MY_PREFS_NAME").commit();
        getSharedPreferences(GlobalHelper.getUserProfile(getApplicationContext()).getProfile().getFirstName(), MODE_PRIVATE).edit().clear().commit();
        GlobalHelper.saveUserInLocal(getApplicationContext(), null, false);


        Intent intent = new Intent(Main_Apps.this, Login.class);

        finishAffinity();
        startActivity(intent);

        ProductDetailCartCounter = 0;
        startActivity(intent);
    }

    public void SearchDialog(final Context context) {
        final Dialog searchPopup = new Dialog(context);
        searchPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchPopup.setContentView(R.layout.search_dialog);
        searchPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        searchPopup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        searchPopup.setCancelable(false);
        final ImageView btn_close = (ImageView) searchPopup.findViewById(R.id.iv_rp_close);
        final EditText email = (EditText) searchPopup.findViewById(R.id.et_search_db);
        Button sucess = (Button) searchPopup.findViewById(R.id.btn_search);
        sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HelpCenterFragment.LoadSearchHelpCenterData(email.getText().toString());
                searchPopup.dismiss();


            }
        });
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    HelpCenterFragment.LoadSearchHelpCenterData(email.getText().toString());
                    searchPopup.dismiss();
                }
                return false;
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPopup.dismiss();
            }
        });
        searchPopup.show();
    }

    public void forgotPasswordDialog(final Context context) {
        final Dialog forgotPasswordPopup = new Dialog(context);
        forgotPasswordPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotPasswordPopup.setContentView(R.layout.guest_user_dialog);
        forgotPasswordPopup.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        forgotPasswordPopup.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        forgotPasswordPopup.setCancelable(false);
        final ImageView btn_close = (ImageView) forgotPasswordPopup.findViewById(R.id.iv_rp_close);
        Button btn_guest_register = (Button) forgotPasswordPopup.findViewById(R.id.btn_guest_register);
        Button btn_guest_login = (Button) forgotPasswordPopup.findViewById(R.id.btn_guest_login);
        btn_guest_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main_Apps.this, Login.class);
                startActivity(i);


            }
        });
        btn_guest_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main_Apps.this, Register.class);
                startActivity(i);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordPopup.dismiss();
            }
        });
        forgotPasswordPopup.show();
    }
}
