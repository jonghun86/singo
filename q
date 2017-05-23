[33mcommit db2e449f9718219ae573cbf3dc42d98d88781f04[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Tue May 23 01:23:49 2017 +0900

    SUCCESS WRITING A REPORT

[33mcommit 12f23706810d6a72b98461641a038b44353bc6f2[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Tue May 23 01:00:45 2017 +0900

    Source code refactoring
     - make transaction package

[33mcommit 209a66af5d724220ef567dc989a1f3d89ade34ee[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Tue May 23 00:56:49 2017 +0900

    - Implement GetQuery to get dupInfo that is inserted to UPcRecommendOrg packet.
    - Timing problem still exist between login and upload video

[33mcommit bc18b9ff0a3d04c1d6475ed4951f9e5a4ff0406c[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sun May 21 02:43:14 2017 +0900

    Source refactoring
      - remove unused codes
      - add error handling codes
      - ERRORs are included

[33mcommit d465acb8fdedda1af8e1112adfcbab1fdf44ac71[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sun May 21 02:12:29 2017 +0900

    Source refactoring
      - remove unused codes
      - add error handling codes
      - ERRORs are included

[33mcommit 79da19287e575f74b09a1f323fcf8c513950f20f[m
Merge: b27dcc8 7aa3652
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 20 21:29:30 2017 +0900

    Merge branch 'login' of https://github.com/jonghun86/singo into login

[33mcommit b27dcc81c0052367bfedd1c02ca2c71f0433bfb8[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 20 17:53:40 2017 +0900

    Source refactoring
      - remove unused codes
      - add error handling codes

[33mcommit 7aa36520fc50556719bae7db6b010bf45709ad24[m
Author: younki.ku <6883205@gmail.com>
Date:   Sat May 20 20:24:25 2017 +0900

    Add Debug Message for POST_TRANSACTION

[33mcommit 1f4c4c37181abc98c481d115b444d782c5ce5e5c[m
Author: younki.ku <6883205@gmail.com>
Date:   Sat May 20 20:08:20 2017 +0900

    Add mid request, final request.
    Occur error in mid response.

[33mcommit 68b964e5b4c49f884338bfe774083d1d3d598044[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 20 15:48:04 2017 +0900

    Debug post variables in reportWrite Activity.
      - add comments for valuePair
      - first request to "UPcRecommendOrg.jsp" is success

[33mcommit 6d5bd5b7bc17dbc9574875b19e00a54d96c11498[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 20 15:21:32 2017 +0900

    Debug post variables in reportWrite Activity.
      - add comments for valuePair
      - first request to "UPcRecommendOrg.jsp" is success

[33mcommit d64d927427db17a093c4e9e78c73f9d1eb35602a[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 19 00:07:18 2017 +0900

    Debug post variables in reportWrite Activity.
      - add comments for valuePair

[33mcommit f82f19fd32fb08023ebd6a9e65a31024ff938e33[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Wed May 17 01:14:49 2017 +0900

    Debug post variables in reportWrite Activity.
      - add comments for valuePair

[33mcommit 8c8a0a60e351f5a236f21c16181d8446e2627248[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Mon May 15 00:59:03 2017 +0900

    Debug post variables in reportWrite Activity.
      - add comments for valuePair

[33mcommit 0edaa27a55fe8e61c5a2e1e7ef96ab017d56421f[m
Author: younki.ku <6883205@gmail.com>
Date:   Sun May 14 19:56:59 2017 +0900

    all the information is revealed except for dubinfo

[33mcommit bae98982ab95dd0b9e68f2b7670859374b91629c[m
Author: younki.ku <6883205@gmail.com>
Date:   Sun May 14 19:44:42 2017 +0900

    add description for some parameters

[33mcommit f8313e67dfa8be4073f319ec9d150b230f2cfe48[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 13 16:27:01 2017 +0900

    Implement reportWrite function
     - write comments ("WTF" annotation means that we have to analyze syntax)

[33mcommit cc81579cb411d23e69cfd4cac806e853b81b8ddf[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 13 15:55:57 2017 +0900

    Implement reportWrite function
     - reorder report value pair (id and value) according to fiddler
     - remove unused vlaue pair

[33mcommit 795bff099fd8a57211ad8f53090183a941c75455[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 12 12:59:10 2017 -0700

    Implementation of multi-part functionality
          - use MultipartEntityBuilder instead of Entry when multimedia data are transmitted
          - file upload success

[33mcommit 9d8c529111a374c9ad05a165b1721b2390651712[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 12 00:44:45 2017 +0900

    Implementation of multi-part functionality
      - use MultipartEntityBuilder instead of Entry when multimedia data are transmitted
      - this commit includes some errors!!
      - this commit will be reverted

[33mcommit eaeee3d305e16f343f0dbc3b9f0deb7bcd8f0282[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 12 00:17:20 2017 +0900

    Implementation of multi-part functionality
      - use MultipartEntityBuilder instead of Entry when multimedia data are transmitted
      - this commit includes some errors!!

[33mcommit 5c9568372a28011654736c6c2e7955c151608f94[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 12 00:02:43 2017 +0900

    Implementation of multi-part functionality

[33mcommit d1cb80264c48795f962285d7c12ae04a6314708f[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 11 23:29:35 2017 +0900

    upgrade http-client version
     - 'httpclient-android', version: '4.3.5'
     - 'httpmime', version: '4.3.6'

[33mcommit 7f3a344c27443d9b583ca8099cd7bb8180d2d2ae[m
Author: younki.ku <6883205@gmail.com>
Date:   Thu May 11 21:48:54 2017 +0900

    change some comments

[33mcommit 199f5368841097a18cd46047cf9731b87c4933cb[m
Author: younki.ku <6883205@gmail.com>
Date:   Thu May 11 21:47:28 2017 +0900

    commit for log
    - print response for send file, submit

[33mcommit 814121290a082bcf0a6a3377afe8c58fefabed7c[m
Author: younki.ku <6883205@gmail.com>
Date:   Thu May 11 21:42:30 2017 +0900

    init commit for report

[33mcommit b188715493995eccd673ecfe91b7a22f80a048c9[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 6 05:57:00 2017 +0900

    User interface
      - print request data in view
      - add report_write Activity
      - connect all activities

[33mcommit af90ea594f576c09f1a37428ea5fd1f92badb184[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 6 05:35:16 2017 +0900

    User interface
      - print request data in view
      - add report_write Activity

[33mcommit 753ea1e31b20c20f339ae23d9a749548c097a7f1[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sat May 6 04:03:48 2017 +0900

    User interface
      - print request data in view

[33mcommit f3fe82add85ba193548bed7ea3c9753d872894f6[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 5 19:35:14 2017 -0700

    Custom layout
     - add yellow line

[33mcommit ef9ed4c9bd6c2b01bb6f72208c67843ad855799d[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri May 5 16:09:20 2017 -0700

    1) upgrade graddle version for mac
    2) rename xml file

[33mcommit bffedd38bfa3de84dfcf59100483970bbe975891[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 4 16:03:46 2017 +0900

    modification of custom layout

[33mcommit d49d7c295a56b840ae9da73b7c671be12efeaed1[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 4 15:19:33 2017 +0900

    missing a file

[33mcommit 851678c97cde4b4e4f11a3e3cfce2543c257d71b[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 4 15:18:13 2017 +0900

    Layout changes
     - custom layout
     - file name refactoring

[33mcommit 12ced55975098c52f9e16ff18b6c7fa2c3eafb12[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 4 14:11:49 2017 +0900

    Layout changes
     - custom layout

[33mcommit 065e64aa7a92b68f4f0c2be8280e95cd34ffcf2b[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu May 4 12:51:26 2017 +0900

    Layout changes

[33mcommit 3253b8f8d003fdc6fa5f1049050282feb1d18120[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Tue May 2 02:13:07 2017 +0900

    UI code for login activity

[33mcommit a3687aa2d2a0575584daffff410226c95b86e063[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Fri Apr 28 14:07:40 2017 +0900

    Add jsuop external library to parse report result from html code

[33mcommit 146654c2e4870b26ca4779aa1ffbec376fd6f270[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Wed Apr 26 00:44:23 2017 +0900

    1. Refactoring of PostTransaction class
    2. Report request success

[33mcommit 50e4d975ec6977b75aef72dd23e85d66c68f31af[m
Author: younki.ku <6883205@gmail.com>
Date:   Tue Apr 25 20:38:38 2017 +0900

    capture / parameters

[33mcommit 91a08e48b55b46900119bb4416a808bf69ab1726[m
Author: younki.ku <6883205@gmail.com>
Date:   Tue Apr 25 20:31:08 2017 +0900

    fiddler inspection file

[33mcommit 72b1e82d2d1e134aef799a45bc1e73ede8bad00f[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Mon Apr 24 00:27:03 2017 +0900

    login success

[33mcommit 74434d978ffc27fc2d9c9d18c9ac947c084fc475[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu Apr 20 23:12:28 2017 +0900

    add test_web_view to see if login session is hold.

[33mcommit f11444b7fc0f79556ab6569c4570dbade3afd8e8[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sun Apr 16 23:42:26 2017 +0900

    Cookie acquired

[33mcommit c4117787bc2f695c2b7b68c10444f45768d9aa10[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Wed Apr 12 00:44:34 2017 +0900

    Implements ReportListActivity #1
     - Get method is used
     - reference :
       https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28

[33mcommit ac3b9271d9bc2717d1b289d2260d8732bf6ec62f[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Tue Apr 11 23:27:43 2017 +0900

    Add reportListActivity
     - The activity shows reports list.
     - To be implemented

[33mcommit aedf95d3bff73f9a25b44dd65bc1e05a67060cb0[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sun Apr 9 17:45:14 2017 +0900

    response check
     - Here : String responseBody = EntityUtils.toString(entity);

[33mcommit 2224731a06601b34ac13c246e06d06ec22d9392d[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Thu Apr 6 00:26:38 2017 +0900

    login response
     - send a request and receive a response.

[33mcommit 92008d038b0de9c01dd67a37d6c030b3dcbf5fdc[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Mon Apr 3 00:35:05 2017 +0900

    Remove unsed code
     - id register code
    Add authority of network access

[33mcommit 4ac7f615b0c564c6389d29019ddd3cd1b5273276[m
Author: jonghun.yoo <mmyjh86@gmail.com>
Date:   Sun Apr 2 22:24:29 2017 +0900

    initial login activity created by android studio
