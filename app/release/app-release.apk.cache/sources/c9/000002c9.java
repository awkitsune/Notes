package com.awkitsune.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.activity.result.C0057d;
import androidx.activity.result.InterfaceC0055b;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import com.awkitsune.notes.MainActivity;
import com.awkitsune.notes.R;
import com.google.android.material.bottomsheet.DialogC0649b;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import p007b1.C0572i;
import p008b2.C0587i;
import p009c.C0606b;
import p017e.AbstractC0833f;
import p017e.ActivityC0831d;
import p050m1.C1229b;
import p052n.AbstractC1237g;
import p074s2.C1417c;
import p074s2.C1421g;
import p074s2.C1425k;
import p074s2.C1426l;
import p074s2.C1428n;
import p074s2.InterfaceC1422h;
import p084v2.C1481g;
import p084v2.C1486j;
import p091x2.C1571a;
import p097z0.C1677e;
import p097z0.C1679g;
import p097z0.C1681i;
import p097z0.C1684k;
import p097z0.View$OnClickListenerC1674b;

/* loaded from: classes.dex */
public final class MainActivity extends ActivityC0831d {

    /* renamed from: v */
    public static final /* synthetic */ int f2307v = 0;

    /* renamed from: q */
    public final String f2308q = "1038C0E34658923C4192E61B16846";

    /* renamed from: r */
    public C1681i f2309r;

    /* renamed from: s */
    public View f2310s;

    /* renamed from: t */
    public DialogC0649b f2311t;

    /* renamed from: u */
    public final C0057d f2312u;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [z0.a] */
    public MainActivity() {
        C0606b c0606b = new C0606b();
        ?? r1 = new InterfaceC0055b() { // from class: z0.a
            @Override // androidx.activity.result.InterfaceC0055b
            /* renamed from: a */
            public final void mo15a(Object obj) {
                String m1570e;
                Uri uri = (Uri) obj;
                int i = MainActivity.f2307v;
                MainActivity mainActivity = MainActivity.this;
                C1421g.m304d(mainActivity, "this$0");
                if (uri != null) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mainActivity.getContentResolver().openInputStream(uri)));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine != null) {
                                sb.append(readLine);
                                sb.append("\n");
                            } else {
                                bufferedReader.close();
                                String sb2 = sb.toString();
                                C1421g.m305c(sb2, "stringBuilder.toString()");
                                C0587i c0587i = C1684k.f5124a;
                                C1684k.C1685a.m6a(sb2);
                                C1684k.C1685a.m5b();
                                mainActivity.m1524r();
                                Toast.makeText(mainActivity, mainActivity.getString(R.string.message_import) + '\n' + uri.getPath(), 0).show();
                                return;
                            }
                        }
                    } catch (Exception e) {
                        Charset charset = C1571a.f4946a;
                        byte[] bytes = "arctf".getBytes(charset);
                        C1421g.m305c(bytes, "this as java.lang.String).getBytes(charset)");
                        byte[] bytes2 = e.toString().getBytes(charset);
                        C1421g.m305c(bytes2, "this as java.lang.String).getBytes(charset)");
                        int length = bytes.length;
                        int length2 = bytes2.length;
                        byte[] copyOf = Arrays.copyOf(bytes, length + length2);
                        System.arraycopy(bytes2, 0, copyOf, length, length2);
                        C1421g.m305c(copyOf, "result");
                        if (copyOf.length == 64) {
                            m1570e = "eyJhbGciOiJIUzI1NiJ9";
                        } else {
                            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                            messageDigest.update(copyOf);
                            byte[] digest = messageDigest.digest();
                            C1421g.m305c(digest, "hashtext");
                            m1570e = C0572i.m1570e(digest);
                        }
                        Log.d("totally not a secret key uwu", m1570e);
                        Log.e("shitty files", e.toString());
                        Toast.makeText(mainActivity, mainActivity.getString(R.string.error_import), 0).show();
                    }
                }
            }
        };
        ComponentActivity.C0043b c0043b = this.f120j;
        this.f2312u = c0043b.m2626c("activity_rq#" + this.f119i.getAndIncrement(), this, c0606b, r1);
    }

    @Override // androidx.fragment.app.ActivityC0369q, androidx.activity.ComponentActivity, p088x.ActivityC1565e, android.app.Activity
    public final void onCreate(Bundle bundle) {
        boolean z;
        C0587i c0587i;
        SharedPreferences sharedPreferences;
        Type m244b;
        Context applicationContext = getApplicationContext();
        C1421g.m305c(applicationContext, "this.applicationContext");
        Signature[] signatureArr = applicationContext.getPackageManager().getPackageInfo(getPackageName(), 64).signatures;
        C1421g.m305c(signatureArr, "packageInfo.signatures");
        if (signatureArr.length > 0) {
            byte[] byteArray = signatureArr[0].toByteArray();
            C1421g.m305c(byteArray, "signature.toByteArray()");
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(byteArray);
            byte[] digest = messageDigest.digest();
            C1421g.m305c(digest, "hashtext");
            z = C1421g.m307a(this.f2308q, C0572i.m1570e(digest));
        } else {
            z = false;
        }
        if (z) {
            Log.d("vj8bHtU1YuIHK8u6aLB5yIRHgGlpyAacDQ1BYuzYQpstcLxf3J2Eu1eLd0ra8e2A", "{}");
        }
        if (AbstractC0833f.f3175c != 2) {
            AbstractC0833f.f3175c = 2;
            synchronized (AbstractC0833f.f3177e) {
                Iterator<WeakReference<AbstractC0833f>> it = AbstractC0833f.f3176d.iterator();
                while (true) {
                    AbstractC1237g.C1238a c1238a = (AbstractC1237g.C1238a) it;
                    if (!c1238a.hasNext()) {
                        break;
                    }
                    AbstractC0833f abstractC0833f = (AbstractC0833f) ((WeakReference) c1238a.next()).get();
                    if (abstractC0833f != null) {
                        abstractC0833f.mo1190d();
                    }
                }
            }
        }
        C1421g.m305c(getSharedPreferences("AppSettings", 0), "getSharedPreferences(Con…ES, Context.MODE_PRIVATE)");
        C0587i c0587i2 = C1684k.f5124a;
        SharedPreferences sharedPreferences2 = getSharedPreferences("Notes", 0);
        C1421g.m305c(sharedPreferences2, "getSharedPreferences(Con…ES, Context.MODE_PRIVATE)");
        C1684k.f5125b = sharedPreferences2;
        try {
            c0587i = C1684k.f5124a;
            sharedPreferences = C1684k.f5125b;
        } catch (Exception e) {
            Log.d("notes loading", e.toString());
        }
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString("Notes", "");
            int i = C1481g.f4786c;
            C1426l c1426l = C1425k.f4613a;
            c1426l.getClass();
            C1481g c1481g = new C1481g(1, new C1428n(new C1417c(C1679g.class), Collections.emptyList()));
            c1426l.getClass();
            C1428n c1428n = new C1428n(new C1417c(ArrayList.class), Collections.singletonList(c1481g));
            if (!(c1428n instanceof InterfaceC1422h) || (m244b = ((InterfaceC1422h) c1428n).m300c()) == null) {
                m244b = C1486j.m244b(c1428n, false);
            }
            Object m1547b = c0587i.m1547b(string, m244b);
            C1421g.m306b(m1547b);
            C1684k.f5126c = (ArrayList) m1547b;
            setTheme(2131755479);
            super.onCreate(bundle);
            setContentView(R.layout.activity_main);
            m1211q().mo1172w((Toolbar) findViewById(R.id.toolbar));
            View inflate = getLayoutInflater().inflate(R.layout.fragment_add_note_dialog_list_dialog, (ViewGroup) null);
            C1421g.m305c(inflate, "layoutInflater.inflate(R…dialog_list_dialog, null)");
            this.f2310s = inflate;
            this.f2311t = new DialogC0649b(this);
            ((RecyclerView) findViewById(R.id.recyclerViewNotes)).m1885h(new C1677e((FloatingActionButton) findViewById(R.id.floatingActionButton)));
            ((FloatingActionButton) findViewById(R.id.floatingActionButton)).setOnClickListener(new View$OnClickListenerC1674b(0, this));
            m1524r();
            return;
        }
        C1421g.m301g("notes_save");
        throw null;
    }

    @Override // android.app.Activity
    public final boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        C1421g.m305c(menuInflater, "menuInflater");
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public final boolean onOptionsItemSelected(MenuItem menuItem) {
        String str;
        C1421g.m304d(menuItem, "item");
        switch (menuItem.getItemId()) {
            case R.id.action_export /* 2131230782 */:
                File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                C1421g.m306b(externalFilesDir);
                if (!externalFilesDir.exists()) {
                    externalFilesDir.mkdirs();
                }
                File file = new File(externalFilesDir, "notes_backup.json");
                try {
                    if (!file.exists() && !file.createNewFile()) {
                        throw new IOException("Cant able to create file");
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    try {
                        str = C1684k.f5124a.m1543f(C1684k.f5126c);
                        C1421g.m305c(str, "gson.toJson(notes)");
                    } catch (Exception e) {
                        Log.d("export error", e.toString());
                        str = "";
                    }
                    byte[] bytes = str.getBytes(C1571a.f4946a);
                    C1421g.m305c(bytes, "this as java.lang.String).getBytes(charset)");
                    fileOutputStream.write(bytes);
                    fileOutputStream.close();
                    Log.e("TAG", "File Path= " + file);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    Toast.makeText(this, getString(R.string.error_export), 0).show();
                }
                Toast.makeText(this, getString(R.string.message_export) + '\n' + file, 1).show();
                break;
            case R.id.action_import /* 2131230784 */:
                this.f2312u.m2629o();
                break;
            case R.id.action_info /* 2131230785 */:
                C1229b c1229b = new C1229b(this);
                AlertController.C0066b c0066b = c1229b.f222a;
                c0066b.f209d = "Notes";
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: z0.c
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        int i2 = MainActivity.f2307v;
                    }
                };
                c0066b.f211f = c0066b.f206a.getText(17039370);
                c0066b.f212g = onClickListener;
                c1229b.mo649a().show();
                break;
        }
        return true;
    }

    /* renamed from: r */
    public final void m1524r() {
        this.f2309r = new C1681i(this);
        View findViewById = findViewById(R.id.recyclerViewNotes);
        C1421g.m305c(findViewById, "findViewById(R.id.recyclerViewNotes)");
        RecyclerView recyclerView = (RecyclerView) findViewById;
        C1681i c1681i = this.f2309r;
        if (c1681i != null) {
            recyclerView.setAdapter(c1681i);
        } else {
            C1421g.m301g("adapter");
            throw null;
        }
    }
}