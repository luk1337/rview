/*
 * Copyright (C) 2016 Jorge Ruesga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruesga.rview.misc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.ruesga.rview.R;

import java.util.Locale;

public class AndroidHelper {

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings("deprecation")
    public static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        }
        return context.getResources().getConfiguration().locale;
    }

    public static void openUriInCustomTabs(Activity activity, String uri) {
        openUriInCustomTabs(activity, Uri.parse(uri));
    }

    public static void openUri(Context ctx, String uri) {
        openUri(ctx, Uri.parse(uri));
    }

    public static void openUriInCustomTabs(Activity activity, Uri uri) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setShowTitle(true);
            builder.setToolbarColor(ContextCompat.getColor(activity, R.color.primaryDark));
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(activity, uri);

        } catch (ActivityNotFoundException ex) {
            // Fallback to default browser
            openUri(activity, uri);
        }
    }

    public static void openUri(Context ctx, Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ctx.startActivity(intent);

        } catch (ActivityNotFoundException ex) {
            // Fallback to default browser
            String msg = ctx.getString(R.string.exception_browser_not_found, uri.toString());
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showErrorSnackbar(Context context, @NonNull View parent,
            @StringRes int message) {
        Snackbar snackbar =  Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View v = snackbar.getView();
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
        snackbar.show();
    }

    public static void showWarningSnackbar(Context context, @NonNull View parent,
            @StringRes int message) {
        Snackbar snackbar =  Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View v = snackbar.getView();
        v.setBackgroundColor(ContextCompat.getColor(context, R.color.alert));
        snackbar.show();
    }

    public static Uri buildUriAndEnsureScheme(String src) {
        Uri uri = Uri.parse(src);
        if (uri.getScheme() == null) {
            // Assume we are talking about an http url
            uri = Uri.parse("http://" + src);
        }
        return uri;
    }
}
