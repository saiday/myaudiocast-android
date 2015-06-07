package saiday.myaudaiocast.myaudiocast.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.provider.MediaStore;
import android.widget.RemoteViews;

import java.lang.ref.WeakReference;

import saiday.myaudaiocast.myaudiocast.R;
import saiday.myaudaiocast.myaudiocast.audio.AudioPlayer;
import saiday.myaudaiocast.myaudiocast.audio.PlayerItem;

/**
 * Created by saiday on 15/6/7.
 */
public class MediaControlsNotification {
    public boolean isNotificationShown;
    private static final String TAG = MediaControlsNotification.class.getSimpleName();
    private static final int mNotificationID = 108;
    private NotificationManager mNotificationManager;
    private Context mContext;
    private final WeakReference<AudioPlayer> mAudioPlayer;

    public static final String INTENT_TOGGLE_PLAY = "INTENT_TOGGLE_PLAY";
    public static final String INTENT_PREVIOUS = "INTENT_PREVIOUS";
    public static final String INTENT_NEXT = "INTENT_NEXT";
    public static final String INTENT_STOP = "INTENT_STOP";

    public MediaControlsNotification(Context context, AudioPlayer audioPlayer) {
        mContext = context;
        mNotificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mAudioPlayer = new WeakReference<>(audioPlayer);
        final AudioPlayer player = this.mAudioPlayer.get();
        player.registerNotification(this);
        isNotificationShown = false;
    }

    public void showNotification() {
        isNotificationShown = true;
        updateNotification();
    }

    public void cancelNotification() {
        isNotificationShown = false;
        cancel();
    }

    public void onEvent(AudioPlayer.PlayerState state) {
        switch (state) {
            case STARTED:
            case PAUSED:
                updateNotification();
                break;
            case COMPLETE:
                cancelNotification();
                break;
            default:
                break;
        }
    }

    private void updateNotification() {
        if (isNotificationShown) {
//            final AudioPlayer audioPlayer = this.mAudioPlayer.get();
//            PlayerItem currentPlayerItem = audioPlayer.getCurrentPlayerItem();
//            boolean isPlaying = audioPlayer.isPlaying();
//            MediaControlsNotification.DisplayFactory displayFactory = new MediaControlsNotification.DisplayFactory
//                    (currentPlayerItem, mContext);
//            RemoteViews view = displayFactory.createContent(currentPlayerItem);
//            RemoteViews bigView = displayFactory.createBigContent(currentPlayerItem);
//            displayFactory.updateItem(view, bigView, isPlaying);
//            displayNotification(view, bigView);
        }
    }

    private boolean displayNotification(RemoteViews remoteView, RemoteViews bigView) {
        boolean display;

        Notification.Builder builder = new Notification.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContent(remoteView)
                .setAutoCancel(false)
                .setOngoing(true);
        Notification notification = builder.build();
        notification.bigContentView = bigView;
        mNotificationManager.notify(mNotificationID, notification);
        display = true;

        return display;
    }

    private void cancel() {
        mNotificationManager.cancel(mNotificationID);
    }

//    public static class DisplayFactory {
//        AudioMessagePlayerItem mPlayerItem;
//        Context mContext;
//
//        public DisplayFactory(AudioMessagePlayerItem playerItem, Context context) {
//            mPlayerItem = playerItem;
//            mContext = context;
//        }
//
//        public RemoteViews createContent(AudioMessagePlayerItem playerItem) {
//            MediaControlsWidget mediaControlsWidget = new MediaControlsWidget(mContext, mContext.getPackageName(),
//                    R.layout.media_controls_notification, playerItem);
//            return mediaControlsWidget;
//        }
//
//        public RemoteViews createBigContent(AudioMessagePlayerItem playerItem) {
//            MediaControlsWidget bigMediaControlsWidget = new MediaControlsWidget(mContext, mContext.getPackageName(),
//                    R.layout.media_controls_notification_large, playerItem);
//            return bigMediaControlsWidget;
//        }
//
//        public void updateItem(RemoteViews views, RemoteViews bigViews, boolean isPlaying) {
//            MediaControlsWidget notificationView = (MediaControlsWidget) views;
//            MediaControlsWidget bigNotificationView = (MediaControlsWidget) bigViews;
//            if (mPlayerItem!= null) {
//                ManagerMaster managerMaster = ManagerMaster.getInstance(mContext);
//                Message message = mPlayerItem.getMessage();
//                Loop loop = managerMaster.getLoopManager().getLoop(message.getLoop().getLoopId());
//                User user = managerMaster.getUserManager().getUser(message.getSender().getUserId());
//                String name = loop.getType() == HedwigDataFactory.LOOP_TYPE_PRIVATE
//                        ? mContext.getString(R.string.private_loop) : message.getLoop().getName();
//                Bitmap profileImageBitmap = NotificationHelper.getSenderPicture(mContext, user);
//                updateNotification(notificationView, message.getSender().getName(), name, profileImageBitmap, isPlaying);
//                updateNotification(bigNotificationView, message.getSender().getName(), name, profileImageBitmap, isPlaying);
//            }
//        }
//
//        private void updateNotification(MediaControlsWidget mediaControlsWidget, String senderName, String loopName, Bitmap profileImage, boolean isPlaying) {
//            mediaControlsWidget.setTextViewText(R.id.notification_title, senderName);
//            mediaControlsWidget.setTextViewText(R.id.notification_loop, loopName);
//            if (profileImage != null) {
//                mediaControlsWidget.setImageViewBitmap(R.id.notification_image, profileImage);
//            }
//
//            int srcID = isPlaying ? R.drawable.ic_miniplayer_pause_active : R.drawable.ic_miniplayer_play_active;
//            mediaControlsWidget.setImageViewResource(R.id.control_play, srcID);
//        }
//    }
}
