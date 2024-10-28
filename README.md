# Android Task Briefing
## Introduction
This project is the demo of fetching and displaying a list of GitHub Users.

![alt text](./screen_shot.mp4)
<video controls width="500">
  <source src="./screen_shot.mp4" type="video/mp4">
  Your browser does not support the video tag. <a href="./screen_shot.mp4">Download the video</a> instead.
</video>

Here are the revised key points for the project:

1. **Minimum SDK Level**: The project targets a minimum SDK level of 26, ensuring compatibility with Android 8.0 (Oreo) and above.

2. **Jetpack Components**:
    * **Lifecycle**: Utilizes Lifecycle-aware components to observe and handle UI states based on lifecycle changes, improving resource management and UI consistency.
    * **ViewModel**: Manages UI-related data with lifecycle awareness, allowing data to persist across configuration changes, such as screen rotations.

3. **Architecture**: The app follows the MVVM (Model-View-ViewModel) architecture pattern, which promotes separation of concerns. Coroutines are used to handle asynchronous tasks efficiently, providing a cleaner and more scalable architecture.

4. **UI Implementation**:
    * The app launches with an Activity that hosts a `UserFeedFragment` displaying a list of users.
    * `UserFeedAdapter` is set up with an `item_user.xml` layout file to render each user in the list.
    * A `UserProfileFragment` is shown when selecting a specific user in the `RecyclerView`, displaying detailed information for the selected user.

5. **App Container**:
    * The App Container centralizes and encapsulates dependencies, such as repositories, use cases, and ViewModels, simplifying dependency management and facilitating testing, especially for ViewModels.

6. **Unit Testing**:
    * Unit tests are created to verify the logic and functionality of the ViewModel, ensuring accurate data handling, state management, and error handling in different scenarios.


## IDE Requirements
This project runs with Android Studio Kaola Feature Drop Version and above 

