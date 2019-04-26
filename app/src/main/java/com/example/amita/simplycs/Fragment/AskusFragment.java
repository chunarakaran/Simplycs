package com.example.amita.simplycs.Fragment;



import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.amita.simplycs.MainActivity;
import com.example.amita.simplycs.R;

import net.dankito.richtexteditor.android.RichTextEditor;
import net.dankito.richtexteditor.android.toolbar.GroupedCommandsEditorToolbar;
import net.dankito.richtexteditor.callback.GetCurrentHtmlCallback;
import net.dankito.richtexteditor.model.DownloadImageConfig;
import net.dankito.richtexteditor.model.DownloadImageUiSetting;
import net.dankito.utils.android.permissions.IPermissionsService;
import net.dankito.utils.android.permissions.PermissionsService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;


public class AskusFragment extends Fragment
{

    TextView Submit;
    String Subject,Msg,encodedHtml;

    EditText Esubject;
    private RichTextEditor editor;

    private GroupedCommandsEditorToolbar bottomGroupedCommandsToolbar;

    private IPermissionsService permissionsService;

    PromptDialog promptDialog;
    private ProgressDialog pDialog;
    String URL;
    String User_id;
    public static final String PREFS_NAME = "login";

    //volley
    RequestQueue requestQueue;

    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootview = inflater.inflate(R.layout.fragment_askus, container, false);

        Toolbar toolbar = rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Ask Us");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        permissionsService = new PermissionsService(getActivity());

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        User_id = sp.getString("User", "");
        URL = getString(R.string.url);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        requestQueue = Volley.newRequestQueue(getActivity());

        promptDialog = new PromptDialog(getActivity());

        Esubject=(EditText)rootview.findViewById(R.id.input_subject);
        editor = (RichTextEditor)rootview.findViewById(R.id.editor);

        // this is needed if you like to insert images so that the user gets asked for permission to access external storage if needed
        // see also onRequestPermissionsResult() below
        editor.setPermissionsService(permissionsService);

        bottomGroupedCommandsToolbar = (GroupedCommandsEditorToolbar)rootview.findViewById(R.id.editorToolbar);
        bottomGroupedCommandsToolbar.setEditor(editor);

        // you can adjust predefined toolbars by removing single commands
//        bottomGroupedCommandsToolbar.removeCommandFromGroupedCommandsView(CommandName.TOGGLE_GROUPED_TEXT_STYLES_COMMANDS_VIEW, CommandName.BOLD);
//        bottomGroupedCommandsToolbar.removeSearchView();


        editor.setEditorFontSize(20);
        editor.setPadding((4 * (int) getResources().getDisplayMetrics().density));

        // some properties you also can set on editor
        editor.setEditorBackgroundColor(Color.LTGRAY);

//        editor.getJavaScriptExecutor();


//        editor.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rect_grayoutline1));
//        editor.setEditorFontColor(Color.MAGENTA)
//        editor.setEditorFontFamily("cursive")

        // show keyboard right at start up
//        editor.focusEditorAndShowKeyboardDelayed()


        // only needed if you allow to automatically download remote images
        editor.setDownloadImageConfig(new DownloadImageConfig(DownloadImageUiSetting.AllowSelectDownloadFolderInCode,
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "downloaded_images")));


        Submit=(TextView)rootview.findViewById(R.id.submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Vsubject,Vmsg;
                Vsubject=Esubject.getText().toString();
                Vmsg=editor.getCachedHtml();

                if(Vsubject.length()==0)
                {
                    Esubject.requestFocus();
                    Esubject.setError("Please Enter Subject");
                }
                else
                {
                    Msg_Submit();
                }


            }
        });



        return rootview;
    }

    // only needed if you like to insert images from local device so the user gets asked for permission to access external storage if needed

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsService.onRequestPermissionsResult(requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // then when you want to do something with edited html
    private void save() {
        editor.getCurrentHtmlAsync(new GetCurrentHtmlCallback() {

            @Override
            public void htmlRetrieved(@NotNull String html) {
                saveHtml(html);
            }
        });
    }

    private void saveHtml(String html) {
        // ...
        Msg=html;
    }

    public void GetValueFromEditText(){

        Subject=Esubject.getText().toString();

        Msg=editor.getCachedHtml();
        // sets html's encoding so that it gets correctly displayed in browser
        encodedHtml = "<html>\n" +
                " <head>\n  <meta charset=\"utf-8\" /> \n </head>\n" +
                " <body>\n" + Msg + " </body>\n" +
                "</html>";
    }


    public void Msg_Submit(){

        pDialog.setMessage("Please Wait...");
        showDialog();

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL+"api/EditProfile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {

                            JSONObject jObj = new JSONObject(ServerResponse);
                            String success = jObj.getString("success");

                            if(success.equalsIgnoreCase("true"))
                            {

                                promptDialog.setCancelable(false);
                                promptDialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
                                promptDialog.setAnimationEnable(true);
                                promptDialog.setTitleText("Success");
                                promptDialog.setContentText(jObj.getString("message"));
                                promptDialog.setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        dialog.dismiss();
                                    }
                                }).show();

                                hideDialog();

                            }
                            else if (success.equalsIgnoreCase("false")){
                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }


                        }
                        catch (JSONException e)
                        {

                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            hideDialog();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.


                        // Showing error message if something goes wrong.
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Auth", User_id);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                GetValueFromEditText();
                // Adding All values to Params.
                params.put("subject", Subject);
                params.put("encodedHtml", encodedHtml);


                return params;
            }


        };

        // Creating RequestQueue.
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest1);
    }





    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
