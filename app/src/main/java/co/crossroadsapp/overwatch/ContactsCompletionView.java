package co.crossroadsapp.overwatch;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crossroadsapp.overwatch.token.TokenCompleteTextView;
import co.crossroadsapp.overwatch.token.TokenTextView;

import static java.security.AccessController.getContext;

/**
 * Created by sharmha on 10/12/16.
 */

public class ContactsCompletionView extends TokenCompleteTextView<Person> {

    public ContactsCompletionView(Context context) {
        super(context);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Person person) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.contact_token, (ViewGroup) getParent(), false);
        token.setText(person.getEmail());
        return token;
    }

    @Override
    protected Person defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        int index = completionText.indexOf('@');
        return new Person(completionText, completionText);
//        if (index == -1) {
//            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
//            ///return new Person(completionText.substring(0, index), completionText);
//        } else {
//            return new Person(completionText.substring(0, index), completionText);
//        }
    }
}
