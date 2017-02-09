package co.crossroadsapp.overwatch;

import android.content.Context;
import android.content.Intent;

import co.crossroadsapp.overwatch.utils.Constants;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * Created by sharmha on 10/31/16.
 */
public class BranchLinkGeneration {

    private BranchUniversalObject branchUniversalObject;
    private String deepLinkUrl;

    protected void branchGenerate(final Context c, String deepLinkTitle, String deepLinkMsg, String eventId, String subtype, String invitee) {
        LinkProperties linkProperties = new LinkProperties();

        generateBranchObject(deepLinkTitle, deepLinkMsg, eventId, subtype, invitee);

        branchUniversalObject.generateShortUrl(c, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    deepLinkUrl = url;
                    final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    if(url!=null) {
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
                        c.startActivity(Intent.createChooser(sharingIntent, "Share"));
                    }
                }
            }
        });
    }

    private void generateBranchObject(String deepLinkTitle, String deepLinkMsg, String id, String subtype, String invitee) {
        // Create a BranchUniversal object for the content referred on this activity instance as invite
        branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item/12345")
                .setCanonicalUrl("https://branch.io/deepviews")
                .setTitle(deepLinkTitle)
                .setContentDescription(deepLinkMsg)
                .setContentImageUrl(Constants.DEEP_LINK_IMAGE + id!=null?id:"" + ".png")
                //.setContentExpiration(new Date(1476566432000L)) // set contents expiration time if applicable
                .addContentMetadata("activityName", subtype)
                .addContentMetadata("invitees", invitee)
                .addContentMetadata("eventId", id!=null?id:"");
    }
}
