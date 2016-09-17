/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2015. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

#ifndef CAMERASERVER_OO_INL_
#define CAMERASERVER_OO_INL_

namespace cs {

inline std::string VideoProperty::GetName() const {
  m_status = 0;
  return GetPropertyName(m_handle, &m_status);
}

inline bool VideoProperty::GetBoolean() const {
  m_status = 0;
  return GetBooleanProperty(m_handle, &m_status);
}

inline void VideoProperty::SetBoolean(bool value) {
  m_status = 0;
  SetBooleanProperty(m_handle, value, &m_status);
}

inline double VideoProperty::GetDouble() const {
  m_status = 0;
  return GetDoubleProperty(m_handle, &m_status);
}

inline void VideoProperty::SetDouble(double value) {
  m_status = 0;
  SetDoubleProperty(m_handle, value, &m_status);
}

inline double VideoProperty::GetMin() const {
  m_status = 0;
  return GetPropertyMin(m_handle, &m_status);
}

inline double VideoProperty::GetMax() const {
  m_status = 0;
  return GetPropertyMax(m_handle, &m_status);
}

inline double VideoProperty::GetStep() const {
  m_status = 0;
  return GetPropertyStep(m_handle, &m_status);
}

inline double VideoProperty::GetDefault() const {
  m_status = 0;
  return GetPropertyDefault(m_handle, &m_status);
}

inline std::string VideoProperty::GetString() const {
  m_status = 0;
  return GetStringProperty(m_handle, &m_status);
}

inline llvm::StringRef VideoProperty::GetString(
    llvm::SmallVectorImpl<char>& buf) const {
  m_status = 0;
  return GetStringProperty(m_handle, buf, &m_status);
}

inline void VideoProperty::SetString(llvm::StringRef value) {
  m_status = 0;
  SetStringProperty(m_handle, value, &m_status);
}

inline int VideoProperty::GetEnum() const {
  m_status = 0;
  return GetEnumProperty(m_handle, &m_status);
}

inline void VideoProperty::SetEnum(int value) {
  m_status = 0;
  SetEnumProperty(m_handle, value, &m_status);
}

inline std::vector<std::string> VideoProperty::GetChoices() const {
  m_status = 0;
  return GetEnumPropertyChoices(m_handle, &m_status);
}

inline VideoProperty::VideoProperty(CS_Property handle) : m_handle(handle) {
  m_status = 0;
  if (handle == 0)
    m_type = kNone;
  else
    m_type =
        static_cast<Type>(static_cast<int>(GetPropertyType(handle, &m_status)));
}

inline VideoSource::VideoSource(const VideoSource& source)
    : m_handle(source.m_handle == 0 ? 0
                                    : CopySource(source.m_handle, &m_status)) {}

inline VideoSource::VideoSource(VideoSource&& other) noexcept : VideoSource() {
  swap(*this, other);
}

inline VideoSource& VideoSource::operator=(VideoSource other) noexcept {
  swap(*this, other);
  return *this;
}

inline VideoSource::~VideoSource() {
  m_status = 0;
  if (m_handle != 0) ReleaseSource(m_handle, &m_status);
}

inline std::string VideoSource::GetName() const {
  m_status = 0;
  return GetSourceName(m_handle, &m_status);
}

inline std::string VideoSource::GetDescription() const {
  m_status = 0;
  return GetSourceDescription(m_handle, &m_status);
}

inline uint64_t VideoSource::GetLastFrameTime() const {
  m_status = 0;
  return GetSourceLastFrameTime(m_handle, &m_status);
}

inline bool VideoSource::IsConnected() const {
  m_status = 0;
  return IsSourceConnected(m_handle, &m_status);
}

inline VideoProperty VideoSource::GetProperty(llvm::StringRef name) {
  m_status = 0;
  return VideoProperty{GetSourceProperty(m_handle, name, &m_status)};
}

inline USBCamera::USBCamera(llvm::StringRef name, int dev) {
  m_handle = CreateUSBSourceDev(name, dev, &m_status);
}

inline USBCamera::USBCamera(llvm::StringRef name, llvm::StringRef path) {
  m_handle = CreateUSBSourcePath(name, path, &m_status);
}

inline std::vector<USBCameraInfo> USBCamera::EnumerateUSBCameras() {
  CS_Status status = 0;
  return ::cs::EnumerateUSBCameras(&status);
}

inline HTTPCamera::HTTPCamera(llvm::StringRef name, llvm::StringRef url) {
  m_handle = CreateHTTPSource(name, url, &m_status);
}

inline CvSource::CvSource(llvm::StringRef name) {
  m_handle = CreateCvSource(name, &m_status);
}

inline void CvSource::PutFrame(cv::Mat* image) {
  m_status = 0;
  PutSourceFrame(m_handle, image, &m_status);
}

inline void CvSource::NotifyError(llvm::StringRef msg) {
  m_status = 0;
  NotifySourceError(m_handle, msg, &m_status);
}

inline void CvSource::SetConnected(bool connected) {
  m_status = 0;
  SetSourceConnected(m_handle, connected, &m_status);
}

inline VideoProperty CvSource::CreateProperty(llvm::StringRef name,
                                              VideoProperty::Type type) {
  m_status = 0;
  return VideoProperty{CreateSourceProperty(
      m_handle, name, static_cast<CS_PropertyType>(static_cast<int>(type)),
      &m_status)};
}

inline VideoProperty CvSource::CreateProperty(
    llvm::StringRef name, VideoProperty::Type type,
    std::function<void(VideoProperty property)> onChange) {
  m_status = 0;
  return VideoProperty{CreateSourcePropertyCallback(
      m_handle, name, static_cast<CS_PropertyType>(static_cast<int>(type)),
      [=](CS_Property property) { onChange(VideoProperty{property}); },
      &m_status)};
}

inline void CvSource::RemoveProperty(VideoProperty property) {
  m_status = 0;
  RemoveSourceProperty(m_handle, property.m_handle, &m_status);
}

inline void CvSource::RemoveProperty(llvm::StringRef name) {
  m_status = 0;
  RemoveSourceProperty(m_handle, name, &m_status);
}

inline VideoSink::VideoSink(const VideoSink& sink)
    : m_handle(sink.m_handle == 0 ? 0 : CopySink(sink.m_handle, &m_status)) {}

inline VideoSink::VideoSink(VideoSink&& other) noexcept : VideoSink() {
  swap(*this, other);
}

inline VideoSink& VideoSink::operator=(VideoSink other) noexcept {
  swap(*this, other);
  return *this;
}

inline VideoSink::~VideoSink() {
  m_status = 0;
  if (m_handle != 0) ReleaseSink(m_handle, &m_status);
}

inline std::string VideoSink::GetName() const {
  m_status = 0;
  return GetSinkName(m_handle, &m_status);
}

inline std::string VideoSink::GetDescription() const {
  m_status = 0;
  return GetSinkDescription(m_handle, &m_status);
}

inline void VideoSink::SetSource(VideoSource source) {
  m_status = 0;
  if (!source)
    SetSinkSource(m_handle, 0, &m_status);
  else
    SetSinkSource(m_handle, source.m_handle, &m_status);
}

inline VideoSource VideoSink::GetSource() const {
  m_status = 0;
  return VideoSource{GetSinkSource(m_handle, &m_status)};
}

inline VideoProperty VideoSink::GetSourceProperty(llvm::StringRef name) {
  m_status = 0;
  return VideoProperty{GetSinkSourceProperty(m_handle, name, &m_status)};
}

inline HTTPSink::HTTPSink(llvm::StringRef name, llvm::StringRef listenAddress,
                          int port) {
  m_handle = CreateHTTPSink(name, listenAddress, port, &m_status);
}

inline CvSink::CvSink(llvm::StringRef name) {
  m_handle = CreateCvSink(name, &m_status);
}

inline CvSink::CvSink(llvm::StringRef name,
                      std::function<void(uint64_t time)> processFrame) {
  m_handle = CreateCvSinkCallback(name, processFrame, &m_status);
}

inline uint64_t CvSink::GrabFrame(cv::Mat* image) const {
  m_status = 0;
  return GrabSinkFrame(m_handle, image, &m_status);
}

inline std::string CvSink::GetError() const {
  m_status = 0;
  return GetSinkError(m_handle, &m_status);
}

inline void CvSink::SetEnabled(bool enabled) {
  m_status = 0;
  SetSinkEnabled(m_handle, enabled, &m_status);
}

inline SourceListener::SourceListener(
    std::function<void(llvm::StringRef name, VideoSource source, int event)>
        callback,
    int eventMask) {
  CS_Status status = 0;
  m_handle = AddSourceListener(
      [=](llvm::StringRef name, CS_Source sourceHandle, int event) {
        callback(name, VideoSource{sourceHandle}, event);
      },
      eventMask, &status);
}

inline SourceListener::SourceListener(SourceListener&& other) noexcept
    : SourceListener() {
  swap(*this, other);
}

inline SourceListener::~SourceListener() {
  CS_Status status = 0;
  if (m_handle != 0) RemoveSourceListener(m_handle, &status);
}

inline SinkListener::SinkListener(
    std::function<void(llvm::StringRef name, VideoSink sink, int event)>
        callback,
    int eventMask) {
  CS_Status status = 0;
  m_handle = AddSinkListener(
      [=](llvm::StringRef name, CS_Sink sinkHandle, int event) {
        callback(name, VideoSink{sinkHandle}, event);
      },
      eventMask, &status);
}

inline SinkListener::SinkListener(SinkListener&& other) noexcept
    : SinkListener() {
  swap(*this, other);
}

inline SinkListener::~SinkListener() {
  CS_Status status = 0;
  if (m_handle != 0) RemoveSinkListener(m_handle, &status);
}

}  // namespace cs

#endif  /* CAMERASERVER_OO_INL_ */
