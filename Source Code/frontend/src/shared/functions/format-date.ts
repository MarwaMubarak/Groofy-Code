const FormatDate = (curDate: string) => {
  const currentDate = new Date();
  const date = new Date(curDate);

  const diffTime = Math.abs(currentDate.getTime() - date.getTime());
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
  const diffHours = Math.floor(diffTime / (1000 * 60 * 60));
  const diffMinutes = Math.floor(diffTime / (1000 * 60));
  const diffSeconds = Math.floor(diffTime / 1000);

  if (diffSeconds < 60) return `${diffSeconds} seconds ago`;
  if (diffMinutes < 60) return `${diffMinutes} minutes ago`;
  if (diffHours < 24) return `${diffHours} hours ago`;

  if (diffDays === 0) {
    return `Today`;
  }
  if (diffDays === 1) {
    return `Yesterday`;
  }
  if (diffDays < 7) {
    return `${diffDays} days ago`;
  }
  if (diffDays < 30) {
    return `${Math.floor(diffDays / 7)} weeks ago`;
  }
  if (diffDays < 365) {
    return `${Math.floor(diffDays / 30)} months ago`;
  }
  if (diffDays >= 365) {
    return `${Math.floor(diffDays / 365)} years ago`;
  }
};
export default FormatDate;
