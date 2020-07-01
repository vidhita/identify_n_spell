package com.game.identifyobject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnDragListener,View.OnLongClickListener {

    public static final String TAG = "MainActivity";
    private String objectName;
    private android.widget.LinearLayout.LayoutParams mLayoutParams;
    private static int shape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shape = getResources().getIdentifier("@drawable/shape", null, this.getPackageName());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "OnCreate Started");

        ImageView img = (ImageView) findViewById(R.id.imageView);

        int imageResource = getResources().getIdentifier("@drawable/sun", null, this.getPackageName());
        img.setImageResource(imageResource);
        objectName = "SUN";
        //DisplayEmptyTable();
        //DisplayLetters();
        DisplayEmptyTable_textview();
        DisplayLetters_textview();
    }

    private void DisplayEmptyTable_textview() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.DestLayout);

        for(int i=0;i<objectName.length();i++)
        {
            TextView txt = new TextView(this);
            txt.setLayoutParams(new android.view.ViewGroup.LayoutParams(150,150));
            txt.setId(i);
            txt.setText(objectName.charAt(i)+"");
            txt.setTag(objectName.charAt(i)+""+i);
            txt.setPadding(2, 2, 2, 2);
            txt.setOnDragListener(this);
            // Adds the view to the layout
            layout.addView(txt);
        }

    }

    private void DisplayLetters_textview() {
        TableLayout table = (TableLayout) findViewById(R.id.SourceTableLayout);

        TableRow row = new TableRow(this);
        table.addView(row);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i=0;i< objectName.length();i++){
            TextView txt = new TextView(this);
            txt.setLayoutParams(new android.view.ViewGroup.LayoutParams(150,150));
            txt.setBackground(getDrawable(shape));
            txt.setTag(i+""+objectName.charAt(i));

            txt.setOnLongClickListener( this);
            row.addView(txt);
        }
    }



    private void DisplayLetters() {
        TableLayout table = (TableLayout) findViewById(R.id.SourceTableLayout);

        TableRow row = new TableRow(this);
        table.addView(row);
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i=0;i< objectName.length();i++){
            ImageView img = new ImageView(this);
            String resName = "@drawable/"+ Character.toLowerCase(objectName.charAt(i));
            //resName = "@drawable/s";
            System.out.println(resName);
            img.setTag(i+""+objectName.charAt(i));
            int imageResource = getResources().getIdentifier(resName, null, this.getPackageName());
            img.setImageResource(imageResource);
            img.setOnLongClickListener( this);
            row.addView(img);
        }
    }


    private void DisplayEmptyTable() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.DestLayout);

        for(int i=0;i<objectName.length();i++)
        {
            ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(150,150));
           // image.setMaxHeight(40);
           // image.setMaxWidth(40);
            String resName = "@drawable/shape";
            //System.out.println(resName);
            int imageResource = getResources().getIdentifier(resName, null, this.getPackageName());
            image.setImageResource(imageResource);
            image.setId(i);
            image.setTag(objectName.charAt(i)+""+i);
            image.setPadding(2, 2, 2, 2);
            image.setOnDragListener(this);
            // Adds the view to the layout
            layout.addView(image);
        }

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
        view.setVisibility(View.INVISIBLE);
        return true;
    }


    //@Override
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
                break;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "ACTION_DROP event");
//                ImageView sourceImg = (ImageView) event.getLocalState();
//                ImageView destImg = (ImageView) view;
//                sourceImg.setVisibility(view.INVISIBLE);
//
//                Drawable d = sourceImg.getDrawable ();
//                if (d != null) {
//                    destImg.setImageDrawable (d);
//                }


                TextView sourceImg = (TextView) event.getLocalState();
                TextView destImg = (TextView) view;
                sourceImg.setVisibility(view.INVISIBLE);

                destImg.setText (sourceImg.getText());


                break;
            default:
                break;
        }
        return true;
    }


}
