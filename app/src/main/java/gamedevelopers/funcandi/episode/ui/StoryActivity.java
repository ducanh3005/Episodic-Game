package gamedevelopers.funcandi.episode.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gamedevelopers.funcandi.episode.PlayAgainActivity;
import gamedevelopers.funcandi.episode.model.Page;
import gamedevelopers.funcandi.episode.model.Story;
import gamedevelopers.funcandi.episode.R;

import java.util.Stack;

/**
 * Created by msk on 04-07-2017.
 */

public class StoryActivity extends AppCompatActivity{

    public static final String TAG = StoryActivity.class.getSimpleName();

    private String name;
    private Story story;
    private ImageView  character;
    private ConstraintLayout c;
    private TextView storyTextView, lesson;
    private Button choice1Button;
    private Button choice2Button;
    private Stack<Integer> pageStack = new Stack<Integer>();

    int x, y,diaCount=1;
    String[] pageText;
    String[] lessons;
    int l=0;

    Typeface t;

    Page page;

    private long animDuration = 1000;

    Intent i, i1;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        context=getApplicationContext();

        i= new Intent(context.getApplicationContext(), EndActivity.class);
        i1= new Intent(context.getApplicationContext(), PlayAgainActivity.class);

        x=getResources().getDisplayMetrics().widthPixels;
        y=getResources().getDisplayMetrics().heightPixels;

        t = Typeface.createFromAsset(getAssets(), "raleway.ttf");

        lessons = new String[4];
        lessons[0]="Lesson Learnt: Be clean! Be healthy!";
        lessons[1]="Lesson Learnt: Always choose wisely";
        lessons[2]="Lesson Learnt: Honesty is the best policy";
        lessons[3]="Lesson Learnt: Prefer even to fail with honor than to win by cheating";


        choice1Button = (Button) findViewById(R.id.choice1Button);
        choice2Button = (Button) findViewById((R.id.choice2Button));
        storyTextView = (TextView) findViewById(R.id.textView);
        lesson = (TextView) findViewById(R.id.textView2);
        character = (ImageView) findViewById(R.id.imageView);
        c = (ConstraintLayout) findViewById(R.id.layout);

        storyTextView.setTypeface(t);
        lesson.setTypeface(t);
        choice1Button.setTypeface(t);
        choice2Button.setTypeface(t);

        choice1Button.setAlpha(0.7f);
        choice2Button.setAlpha(0.7f);


        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        if (name == null || name.isEmpty()) {
            name = " _yourname_ ";
        }
        Log.d(TAG, name);


        lesson.setVisibility(View.INVISIBLE);
        story = new Story(name, context);
        loadPage(0);


    }

    private void loadPage(int pageNumber) {

        if (story.analysis ){

            if (pageNumber>=9)
            callEndActivity();
        }

        pageStack.push(pageNumber);

        diaCount=1;

        Drawable speechbg;
        page = story.getPage(pageNumber);

        if (page.getMainChar() == 1) {
            storyTextView.setWidth(x/2);
            storyTextView.setX(x/2-x/8);

            if (page.isThinking()){
                speechbg = ContextCompat.getDrawable(this, R.drawable.episode_think);
            }
            else {
                speechbg = ContextCompat.getDrawable(this, R.drawable.episode_speechbubble);
            }

            storyTextView.setBackground(speechbg);
            loadmainAniamtion();
            character.setY(y/2-y/14);

        }
        else {

            if (page.getIsBuddy()==1){
                character.setY(y/3);
            }
            else {
                character.setY(y/3);
            }

            storyTextView.setWidth(x/2);
            storyTextView.setX(x/2-x/3);
            //storyTextView.setY(y/4);

            if (page.isThinking()){
                speechbg = ContextCompat.getDrawable(this, R.drawable.episode_think);
            }
            else {
                speechbg = ContextCompat.getDrawable(this, R.drawable.episode_speechbubble1);
            }

            storyTextView.setBackground(speechbg);
            loadotherAniamtion();
        }


        Drawable image = ContextCompat.getDrawable(this, page.getImageId());
        //image = new ScaleDrawable(image, 0, x, y-y/6).getDrawable();
        //image.setBounds(0,0, x, y-y/6);
        c.setBackground(image);


       // Bitmap charImage = BitmapFactory.decodeResource(getResources(), page.getCharId());
       // Bitmap resizedChar = Bitmap.createScaledBitmap(charImage, y/3-y/8, y/2-y/10, false);
        Drawable charImage = ContextCompat.getDrawable(this, page.getCharId());
        character.setBackground(charImage);

        pageText = page.getDialo();
        //storyTextView.setTextSize(x/70);
        lesson.setTextColor(Color.WHITE);
        lesson.setY(30);
        storyTextView.setTextColor(Color.WHITE);
        //storyTextView.setAlpha(0.7f);

        storyTextView.setText(pageText[0]);

        //pageText = String.format(pageText, name);
        //storyTextView.setText(pageText);


        if (page.isFinalPage()) {
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText("NEXT");
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPlayAgainActivity();
                   // loadPage(0);
                }
            });
        }
        else {
            loadButtons(page);
        }

    }

    private void loadButtons(final Page page) {


        int nextPage = page.getChoice2().getNextPage();
        if (nextPage==31 || nextPage==32) {
            lesson.setVisibility(View.VISIBLE);
            lesson.setText(lessons[3]);
        }

        if (diaCount<pageText.length) {
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText("NEXT");

            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storyTextView.setText(pageText[diaCount]);
                    diaCount+=1;
                    loadButtons(page);
                }
            });

        }
        else if (page.getChoice1().getCh().equals("NEXT")) {

            //lesson.setVisibility(View.INVISIBLE);

            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setText("NEXT");

            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = page.getChoice1().getNextPage();
                    lesson.setVisibility(View.INVISIBLE);
                    loadPage(nextPage);
                }
            });
        }
          else  {

                choice1Button.setVisibility(View.VISIBLE);
                choice1Button.setText(page.getChoice1().getCh());
                choice1Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nextPage = page.getChoice1().getNextPage();
                        lesson.setVisibility(View.VISIBLE);

                        if (nextPage==12) {
                            lesson.setText(lessons[0]);
                        }
                        if (nextPage==38) {
                            lesson.setText(lessons[1]);
                        }
                        if (nextPage==33) {
                            lesson.setText(lessons[2]);
                        }

                        loadPage(nextPage);
                    }
                });

                choice2Button.setVisibility(View.VISIBLE);
                choice2Button.setText(page.getChoice2().getCh());
                choice2Button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nextPage = page.getChoice2().getNextPage();

                        loadPage(nextPage);
                    }
                });

            }
        }



    public void loadmainAniamtion() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(character, "x", -20, x/14);
        animatorX.setDuration(animDuration);

        ObjectAnimator textAnimator;
        if (page.isThinking()){
             textAnimator = ObjectAnimator.ofFloat(storyTextView, "y", 0, y/3-y/12);
            textAnimator.setDuration(1500L);
        }
        else {
             textAnimator = ObjectAnimator.ofFloat(storyTextView, "y", y, y-y/3-y/14);
            textAnimator.setDuration(1500L);
        }

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(character, View.ALPHA, 0.0f, 1.0f);
        alphaAnimator.setDuration(animDuration);

        ObjectAnimator alphaTextAnimator = ObjectAnimator.ofFloat(storyTextView, View.ALPHA, 0.0f, 0.8f);
        alphaAnimator.setDuration(1500L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, alphaAnimator, textAnimator, alphaTextAnimator);
        animatorSet.start();
    }

    public void loadotherAniamtion() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(character, "x", x, x-x/3-x/8);
        animatorX.setDuration(animDuration);

        ObjectAnimator textAnimator;
        if (page.isThinking()){
            textAnimator = ObjectAnimator.ofFloat(storyTextView, "y", 0, y/3);
            textAnimator.setDuration(1500L);
        }
        else {
            textAnimator = ObjectAnimator.ofFloat(storyTextView, "y", y, y-y/3-y/8);
            textAnimator.setDuration(1500L);
        }



        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(character, View.ALPHA, 0.0f, 1.0f);
        alphaAnimator.setDuration(animDuration);

        ObjectAnimator alphaTextAnimator = ObjectAnimator.ofFloat(storyTextView, View.ALPHA, 0.0f, 0.8f);
        alphaAnimator.setDuration(1500L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, alphaAnimator, textAnimator, alphaTextAnimator);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if (pageStack.isEmpty()) {
            super.onBackPressed();
        }
        else {
            loadPage(pageStack.pop());
        }

    }

    public void callEndActivity() {

        // i.putExtra("score", score);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(i);
    }

    public void callPlayAgainActivity() {

        // i.putExtra("score", score);
        i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(i1);
    }

}
