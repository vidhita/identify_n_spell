package com.game.identifyobject;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UProperty.INT_START;
import static java.util.stream.Stream.generate;

public class LandingPage extends AppCompatActivity implements View.OnDragListener,View.OnLongClickListener{
    public static final String TAG = "LandingPageActivity";
    private int currentIndex = 0;
    private List<DisplayImage> imageList;
    TableLayout sourceTable, destTable;
    private android.widget.LinearLayout.LayoutParams mLayoutParams;
    private static int DRAG = 1;
    private static int DROP = 2;
    private String dragString;
    private String objectSpelling;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        sourceTable = (TableLayout) findViewById(R.id.SourceTableLayout);
        destTable = (TableLayout) findViewById(R.id.DestTableLayout);

        Log.d(TAG, "OnCreate Started");
        populateObjectList();
        displayObject();
        String value = getShuffledSpelling();
        DisplayLetters(sourceTable, value,LandingPage.DRAG);

        char[] repeat = new char[value.length()];
        Arrays.fill(repeat, ' ');

        DisplayLetters(destTable,  new String(repeat),LandingPage.DROP );
    }

    private String getShuffledSpelling() {
        String value = objectSpelling;//imageList.get(currentIndex).getSpelling();

        ArrayList<Character>  mylist = new ArrayList<Character>();
        for (int i=0;i<value.length();i++) {
            mylist.add(value.charAt(i));
        }
        Collections.shuffle(mylist);
        StringBuilder builder = new StringBuilder(mylist.size());
        for(Character ch: mylist)
        {
            builder.append(ch);
        }
       // DisplayLetters(sourceTable, builder.toString());
        Log.d(TAG, mylist.toString());
        return builder.toString();
    }

    private void DisplayLetters(TableLayout tableLayout, String value, int dragordrop) {

        TableRow row = new TableRow(this);
        tableLayout.addView(row);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        int shape = getResources().getIdentifier("@drawable/shape", null, this.getPackageName());
        for (int i=0;i< value.length();i++){
            TextView txt = new TextView(this);
            txt.setTypeface(txt.getTypeface(), Typeface.BOLD);
            txt.setText(value.charAt(i)+"");
            txt.setPadding(10, 10, 10, 10);
            txt.setTextSize(30);
            txt.setGravity(Gravity.CENTER_HORIZONTAL);
            txt.setWidth(100);
            txt.setBackground(getDrawable(shape));
            txt.setTag(i+""+value.charAt(i));
            //if (dragordrop == LandingPage.DRAG){
                txt.setOnLongClickListener( this);
            //}else{
                txt.setOnDragListener(this);
           // }
            row.addView(txt);
        }
    }

    private void reset() {
        if(((TableLayout) sourceTable).getChildCount() > 0)
            ((TableLayout) sourceTable).removeAllViews();
        if(((TableLayout) destTable).getChildCount() > 0)
            ((TableLayout) destTable).removeAllViews();

    }

    private void displayObject() {
        if (currentIndex >=imageList.size()){
            currentIndex = 0;
        }
        String value = imageList.get(currentIndex).getId();
        ImageView img = (ImageView) findViewById(R.id.imageView);

        int imageResource = getResources().getIdentifier("@drawable/" + value, null, this.getPackageName());
        img.setImageResource(imageResource);
        objectSpelling = imageList.get(currentIndex).getSpelling();
    }

    private void populateObjectList(){
        if (imageList == null) {
            imageList = new ArrayList<>();
        }

        imageList.add(new DisplayImage("sun","SUN"));
        imageList.add(new DisplayImage("drum","DRUM"));

    }

    public void onNext(View view) {
        // Check the answer

        TableRow row = (TableRow)destTable.getChildAt(0); // Here get row id depending on number of row
        int count = row.getChildCount();

        boolean error=false;
        String answer ="" ;
        for (int i=0;i<count;i++){
            TextView txt = (TextView) row.getChildAt(i); // get child index on particular row
            String buttonText = txt.getText().toString();
            if (buttonText.isEmpty()){
                txt.setError("Empty");
                error = true;
            }else {
                answer += buttonText;
            }
        }
        if (!answer.equals(objectSpelling)){
            Toast.makeText(getApplicationContext(),"Incorrect. Try Again ",Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(getApplicationContext(),"Well Done!!",Toast.LENGTH_SHORT).show();
        }

        // Move to next
        currentIndex++;
        reset();
        displayObject();
        String value = getShuffledSpelling();
        DisplayLetters(sourceTable, value,LandingPage.DRAG);

        char[] repeat = new char[value.length()];
        Arrays.fill(repeat, ' ');

        DisplayLetters(destTable,  new String(repeat),LandingPage.DROP );
    }

    public void onExit(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Close Application")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }



    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        //Set view visibility to INVISIBLE as we are going to drag the view

        dragString = ((TextView)view).getText().toString();
        ((TextView)view).setText("");
        //view.setVisibility(View.INVISIBLE);
        return true;
    }


    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                int x_cord = (int) event.getX();
                int y_cord = (int) event.getY();
                break;
            case DragEvent.ACTION_DRAG_EXITED :
                Log.d(TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
                x_cord = (int) event.getX();
                y_cord = (int) event.getY();
                mLayoutParams.leftMargin = x_cord;
                mLayoutParams.topMargin = y_cord;
                view.setLayoutParams(mLayoutParams);
                break;
            case DragEvent.ACTION_DRAG_LOCATION :
                Log.d(TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
                x_cord = (int) event.getX();
                y_cord = (int) event.getY();
                break;
            case DragEvent.ACTION_DRAG_ENDED :
                Log.d(TAG, "Action is DragEvent.ACTION_DRAG_ENDED");
                // Do nothing
                if (!event.getResult()){
                    TextView source = (TextView) event.getLocalState();
                    source.setText(dragString);
                    source.setVisibility(View.VISIBLE);
                }
                break;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION_DROP event");
                TextView source = (TextView) event.getLocalState();
                System.out.println("action drop"+source);
                TextView dest = (TextView) view;

              //  source.setVisibility(view.INVISIBLE);
//
//                Drawable d = sourceImg.getDrawable ();
//                if (d != null) {
//                    destImg.setImageDrawable (d);
//                }
                if (dest.getText().toString().trim().isEmpty()) {
                    dest.setText(dragString);
                    source.setVisibility(View.VISIBLE);
                }else{
                    source.setText(dragString);
                }
                break;
            default:
                break;
        }
        return true;
    }

}
